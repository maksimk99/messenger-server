package com.epam.messenger.websocket;

import com.epam.messenger.model.dto.MessageDTO;
import com.epam.messenger.service.MessageService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketChatController {

    @Value("${conversation.incoming.exchange.name}")
    private String CONVERSATION_INCOMING_EXCHANGE_NAME;

    private final RabbitTemplate template;
    private final MessageService messageService;

    @Autowired
    public WebSocketChatController(final RabbitTemplate template, final MessageService messageService) {
        this.template = template;
        this.messageService = messageService;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(MessageDTO message) {
        this.template.convertAndSend(CONVERSATION_INCOMING_EXCHANGE_NAME, "chat" + message.getChatId(), message);
        messageService.addMessage(message.convertToMessage());
    }
}

