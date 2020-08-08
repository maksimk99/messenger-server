package com.epam.messenger.service;

import com.epam.messenger.model.Chat;
import com.epam.messenger.model.dto.ChatDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChatService {
    Chat findById(Integer chatId);

    List<ChatDTO> findUserChats(Integer userId);

    ChatDTO creatChat(ChatDTO chatDTO);
}
