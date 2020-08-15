package com.epam.messenger.service.impl;

import com.epam.messenger.model.dto.ChatDTO;
import com.epam.messenger.model.dto.ContactDTO;
import com.epam.messenger.model.dto.LoginUserDTO;
import com.epam.messenger.model.dto.LoginUserResponseDTO;
import com.epam.messenger.model.User;
import com.epam.messenger.model.dto.RegisterUserDTO;
import com.epam.messenger.rabbitmq.RabbitMQManager;
import com.epam.messenger.repository.UserRepository;
import com.epam.messenger.service.ChatService;
import com.epam.messenger.service.FileWriter;
import com.epam.messenger.service.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Value("${user.info.exchange.name}")
    private String USER_INFO_EXCHANGE_NAME;
    @Value("${user.info.routing.key.prefix}")
    private String USER_INFO_ROUTING_KEY_PREFIX;

    private final UserRepository userRepository;
    private final ChatService chatService;
    private final FileWriter fileWriter;
    private final RabbitMQManager rabbitMQManager;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository, final ChatService chatService,
                           final FileWriter fileWriter, final RabbitMQManager rabbitMQManager,
                           final RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.chatService = chatService;
        this.fileWriter = fileWriter;
        this.rabbitMQManager = rabbitMQManager;
        this.rabbitTemplate = rabbitTemplate;
    }

    public User findById(final Integer userId) {
        return userRepository.findByUserId(userId).get();
    }

    public ContactDTO findByPhoneNumber(final String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber.replaceFirst(" ", "+"))
                .map(ContactDTO::build).orElse(null);
    }

    public ContactDTO addContactToUser(final Integer userId, final String contactPhoneNumber) {

        Optional<User> contactOptional = userRepository.findByPhoneNumber(contactPhoneNumber
                .replaceFirst(" ", "+"));
        if (contactOptional.isPresent()) {
            User contact = contactOptional.get();
            User user = userRepository.findByUserId(userId).get();
            user.getContacts().add(contact);
            userRepository.save(user);
            rabbitMQManager.bindContactToUser(userId, contact.getUserId());
            return ContactDTO.build(contact);
        } else {
            return null;
        }
    }

    public LoginUserResponseDTO login(final LoginUserDTO loginUserDTO) {
        Optional<User> user = userRepository.findByPhoneNumber(loginUserDTO.getPhoneNumber());
        if (user.isPresent() && user.get().getPassword().equals(loginUserDTO.getPassword())) {
            List<ChatDTO> chats = chatService.findUserChats(user.get().getUserId());
            return LoginUserResponseDTO.build(user.get(), chats);
        }else {
            return null;
        }
    }

    public Integer register(RegisterUserDTO registerUserDTO) {
        Optional<User> user = userRepository.findByPhoneNumber(registerUserDTO.getPhoneNumber());
        if (user.isPresent()) {
            return null;
        } else {
            Integer userId = userRepository.save(registerUserDTO.convertToUser()).getUserId();
            rabbitMQManager.createExchange(userId);
            return userId;
        }
    }

    public User updateUserInfo(final Integer userId, final MultipartFile file, final String userName) {
        User user = userRepository.findByUserId(userId).get();
        if (file != null) {
            fileWriter.deleteFile(user.getAvatarUrl());
            String resultImageUrl = fileWriter.writeFile(file);
            if (resultImageUrl == null) {
                return null;
            } else {
                user.setAvatarUrl(resultImageUrl);
            }
        }
        if (userName != null) {
            user.setUserName(userName);
        }
        userRepository.save(user);
        rabbitTemplate.convertAndSend(USER_INFO_EXCHANGE_NAME, USER_INFO_ROUTING_KEY_PREFIX + userId,
                ContactDTO.build(user), message -> {
                    message.getMessageProperties().getHeaders().put("messageType", "userInfo");
                    return message;
                });
        return user;
    }
}
