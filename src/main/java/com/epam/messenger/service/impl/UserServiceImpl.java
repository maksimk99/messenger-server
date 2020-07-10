package com.epam.messenger.service.impl;

import com.epam.messenger.model.dto.ChatDTO;
import com.epam.messenger.model.dto.ContactDTO;
import com.epam.messenger.model.dto.LoginUserDTO;
import com.epam.messenger.model.dto.LoginUserResponseDTO;
import com.epam.messenger.model.User;
import com.epam.messenger.model.dto.RegisterUserDTO;
import com.epam.messenger.repository.UserRepository;
import com.epam.messenger.service.ChatService;
import com.epam.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ChatService chatService;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository, final ChatService chatService) {
        this.userRepository = userRepository;
        this.chatService = chatService;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(final Integer userId) {
        return userRepository.findByUserId(userId).get();
    }

    public ContactDTO findByPhoneNumber(final String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber.replaceFirst(" ", "+")).map(ContactDTO::build).orElse(null);
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
            return userRepository.save(registerUserDTO.convertToUser()).getUserId();
        }
    }
}
