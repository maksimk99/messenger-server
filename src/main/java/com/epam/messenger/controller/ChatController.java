package com.epam.messenger.controller;

import com.epam.messenger.model.dto.ChatBrokerDTO;
import com.epam.messenger.model.dto.ChatDTO;
import com.epam.messenger.model.dto.MessageDTO;
import com.epam.messenger.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://192.168.43.125:8100")
@RestController
public class ChatController {

    private final ChatService chatService;
    private final RabbitTemplate template;
    private final ObjectMapper objectMapper;

    @Autowired
    public ChatController(final ChatService chatService, final RabbitTemplate template, final ObjectMapper objectMapper) {
        this.chatService = chatService;
        this.template = template;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/chat/create")
    public ChatDTO createChat(@RequestParam(value = "chatDTO") String chatDTO) throws JsonProcessingException {
        return chatService.creatChat(objectMapper.readValue(chatDTO, ChatDTO.class));
    }

    @GetMapping("/test/message/{chatId}/{senderId}/{message}")
    public void testSendMessage(@PathVariable Integer chatId, @PathVariable Integer senderId, @PathVariable String message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessage(message);
        messageDTO.setDateSent(new Timestamp(new Date().getTime()));
        messageDTO.setSenderId(senderId);
        messageDTO.setChatId(chatId);
        this.template.convertAndSend("conversation.incoming", "chat" + chatId, messageDTO);
    }

    @GetMapping("/test/chat/{chatName}")
    public ChatBrokerDTO testCreateChat(@PathVariable String chatName) {
        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setChatName(chatName);
        chatDTO.setMembers(List.of(1, 2, 4, 6));
        Integer chatId = chatService.creatChat(chatDTO).getChatId();
        return ChatBrokerDTO.build(chatService.findById(chatId));
    }

    @GetMapping("/chat/{chatId}")
    public ChatBrokerDTO getContactById(@PathVariable Integer chatId) {
        return ChatBrokerDTO.build(chatService.findById(chatId));
    }
}
