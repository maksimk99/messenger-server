package com.epam.messenger.service.impl;

import com.epam.messenger.model.Chat;
import com.epam.messenger.model.User;
import com.epam.messenger.model.dto.ChatBrokerDTO;
import com.epam.messenger.model.dto.ChatDTO;
import com.epam.messenger.rabbitmq.RabbitMQManager;
import com.epam.messenger.repository.ChatRepository;
import com.epam.messenger.repository.UserRepository;
import com.epam.messenger.service.ChatService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ChatServiceImpl implements ChatService {

    @Value("${conversation.incoming.exchange.name}")
    private String CONVERSATION_INCOMING_EXCHANGE_NAME;
    @Value("${chat.routing.key.prefix}")
    private String CHAT_ROUTING_KEY_PREFIX;

    private final RabbitMQManager rabbitMQManager;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ChatServiceImpl(final ChatRepository chatRepository, final RabbitMQManager rabbitMQManager,
                           final UserRepository userRepository,
                           final RabbitTemplate rabbitTemplate) {
        this.rabbitMQManager = rabbitMQManager;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Chat findById(final Integer chatId) {
        return chatRepository.findById(chatId).get();
    }

    public List<ChatDTO> findUserChats(final Integer userId) {
        return chatRepository.findChatsByUsersUserId(userId).stream().map(ChatDTO::build).collect(Collectors.toList());
    }

    public ChatDTO creatChat(final ChatDTO chatDTO) {
        Chat chat = saveChat(chatDTO);
        List<Integer> chatMembers = chatDTO.getMembers();
        for (Integer chatMember : chatMembers) {
            rabbitMQManager.bindChatToUser(chatMember, chat.getChatId());
            addMembersOfChatToUserContacts(chatMember, chatMembers);
        }
        rabbitTemplate.convertAndSend(CONVERSATION_INCOMING_EXCHANGE_NAME, CHAT_ROUTING_KEY_PREFIX + chat.getChatId(),
                ChatBrokerDTO.build(chat), message -> {
                    message.getMessageProperties().getHeaders().put("messageType", "chat");
                    return message;
                });
        return ChatDTO.build(chat);
    }

    private Chat saveChat(final ChatDTO chatDTO) {
        Chat chat = chatDTO.convertToChat();
        chat.setUsers(chatDTO.getMembers().stream().map(userId -> userRepository.findById(userId).get())
                .collect(Collectors.toList()));
        return chatRepository.save(chat);
    }

    private void addMembersOfChatToUserContacts(final Integer userId, final List<Integer> vChatMembersId) {
        User user = userRepository.findByUserId(userId).get();
        List<Integer> chatMembersId = new ArrayList<>(vChatMembersId);
        List<Integer> userContactsId = user.getContacts().stream().map(User::getUserId).collect(Collectors.toList());
        chatMembersId.removeAll(userContactsId);
        chatMembersId.remove(userId);
        if (chatMembersId.size() > 0) {
            for(Integer memberId: chatMembersId) {
                rabbitMQManager.bindContactToUser(userId, memberId);
                user.getContacts().add(new User().setUserId(memberId));
            }
            userRepository.save(user);
        }
    }
}
