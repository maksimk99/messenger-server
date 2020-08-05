package com.epam.messenger.controller;

import com.epam.messenger.model.Chat;
import com.epam.messenger.model.Message;
import com.epam.messenger.model.User;
import com.epam.messenger.model.dto.ChatBrokerDTO;
import com.epam.messenger.model.dto.ChatDTO;
import com.epam.messenger.model.dto.MessageDTO;
import com.epam.messenger.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://192.168.43.125:8100")
@RestController
public class ChatController {

    private final ChatService chatService;
    private RabbitTemplate template;

    @Autowired
    public ChatController(final ChatService chatService, final RabbitTemplate template) {
        this.chatService = chatService;
        this.template = template;
    }

    @PostMapping("/chat/create")
    public Integer createChat(@RequestBody ChatDTO chatDTO) {
        return chatService.creatChat(chatDTO);
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
        Integer chatId = chatService.creatChat(chatDTO);
        return ChatBrokerDTO.build(chatService.findById(chatId));
    }

    @GetMapping("/chat/{chatId}")
    public ChatBrokerDTO getContactById(@PathVariable Integer chatId) {
        return ChatBrokerDTO.build(chatService.findById(chatId));
    }
}
