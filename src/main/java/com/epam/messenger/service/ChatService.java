package com.epam.messenger.service;

import com.epam.messenger.model.Chat;
import com.epam.messenger.model.dto.ChatDTO;

import java.util.List;

public interface ChatService {
    Chat findById(Integer chatId);

    List<ChatDTO> findUserChats(Integer userId);

    Integer creatChat(ChatDTO chatDTO);
}
