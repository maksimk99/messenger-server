package com.epam.messenger.rabbitmq;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${conversation.incoming.exchange.name}")
    private String CONVERSATION_INCOMING_EXCHANGE_NAME;
    @Value("${user.info.exchange.name}")
    private String USER_INFO_EXCHANGE_NAME;

    @Bean(name = "conversationIncoming")
    public TopicExchange conversationIncoming() {
        return new TopicExchange(CONVERSATION_INCOMING_EXCHANGE_NAME);
    }

    @Bean(name = "userInfo")
    public TopicExchange userInfo() {
        return new TopicExchange(USER_INFO_EXCHANGE_NAME);
    }
    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.declareExchange(conversationIncoming());
        return rabbitAdmin;
    }

    @Bean
    Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
