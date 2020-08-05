package com.epam.messenger.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQManager {

    @Value("${user.exchange.prefix}")
    private String USER_EXCHANGE_PREFIX;
    @Value("${chat.routing.key.prefix}")
    private String CHAT_ROUTING_KEY_PREFIX;

    private final TopicExchange conversationIncoming;
    private final RabbitAdmin rabbitAdmin;

    @Autowired
    public RabbitMQManager(final TopicExchange conversationIncoming, final RabbitAdmin rabbitAdmin) {
        this.conversationIncoming = conversationIncoming;
        this.rabbitAdmin = rabbitAdmin;
    }

    public void createBinding(final Integer userId, final Integer chatId) {
        FanoutExchange userExchange = new FanoutExchange(USER_EXCHANGE_PREFIX + userId);
        Binding binding = BindingBuilder.bind(userExchange).to(conversationIncoming).with(CHAT_ROUTING_KEY_PREFIX + chatId);
        rabbitAdmin.declareBinding(binding);
    }

    public void createExchange(final Integer userId) {
        rabbitAdmin.declareExchange(new FanoutExchange(USER_EXCHANGE_PREFIX + userId));
    }
}
