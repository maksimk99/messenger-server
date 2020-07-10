package com.epam.messenger.controller;

import com.epam.messenger.model.dto.ChatDTO;
import com.epam.messenger.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://192.168.43.125:8100")
@RestController
public class ChatController {

    Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final ChatService chatService;

    @Autowired
    public ChatController(final ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat/create")
    public Integer createChat(@RequestBody ChatDTO chatDTO) {
        logger.info("create chat: " + chatDTO);
        return chatService.creatChat(chatDTO);
    }
}
