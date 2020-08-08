package com.epam.messenger.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQManager {

    @Value("${user.exchange.prefix}")
    private String USER_EXCHANGE_PREFIX;
    @Value("${chat.routing.key.prefix}")
    private String CHAT_ROUTING_KEY_PREFIX;
    @Value("${user.info.routing.key.prefix}")
    private String USER_INFO_ROUTING_KEY_PREFIX;

    private final TopicExchange conversationIncoming;
    private final TopicExchange userInfo;
    private final RabbitAdmin rabbitAdmin;

    @Autowired
    public RabbitMQManager(@Qualifier("conversationIncoming") final TopicExchange conversationIncoming,
                           @Qualifier("userInfo") final TopicExchange userInfo, final RabbitAdmin rabbitAdmin) {
        this.conversationIncoming = conversationIncoming;
        this.userInfo = userInfo;
        this.rabbitAdmin = rabbitAdmin;
    }

    public void createExchange(final Integer userId) {
        rabbitAdmin.declareExchange(new FanoutExchange(USER_EXCHANGE_PREFIX + userId));
        createBinding(userId, USER_INFO_ROUTING_KEY_PREFIX + userId, userInfo);
    }

    public void bindContactToUser(final Integer userId, final Integer contactId) {
        createBinding(userId, USER_INFO_ROUTING_KEY_PREFIX + contactId, userInfo);
    }

    public void bindChatToUser(final Integer userId, final Integer chatId) {
        createBinding(userId, CHAT_ROUTING_KEY_PREFIX + chatId, conversationIncoming);
    }

    private void createBinding(final Integer userId, final String routingKey, final TopicExchange exchangeToBindTo) {
        FanoutExchange userExchange = new FanoutExchange(USER_EXCHANGE_PREFIX + userId);
        Binding binding = BindingBuilder.bind(userExchange).to(exchangeToBindTo).with(routingKey);
        rabbitAdmin.declareBinding(binding);
    }
}
