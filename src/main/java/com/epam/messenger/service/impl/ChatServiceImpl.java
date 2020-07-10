package com.epam.messenger.service.impl;

import com.epam.messenger.model.Chat;
import com.epam.messenger.model.dto.ChatDTO;
import com.epam.messenger.repository.ChatRepository;
import com.epam.messenger.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {

    Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);
    private final ChatRepository chatRepository;

    @Autowired
    public ChatServiceImpl(final ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<Chat> findAll() {
        return chatRepository.findAll();
    }

    public Chat findById(final Integer chatId) {
        return chatRepository.findById(chatId).get();
    }

    public List<ChatDTO> findUserChats(final Integer userId) {
        return chatRepository.findChatsByUsersUserId(userId).stream().map(ChatDTO::build).collect(Collectors.toList());
    }

    public Integer creatChat(final ChatDTO chatDTO) {
        logger.info("convert to chat: " + chatDTO);
        return chatRepository.save(chatDTO.convertToChat()).getChatId();
    }
}
