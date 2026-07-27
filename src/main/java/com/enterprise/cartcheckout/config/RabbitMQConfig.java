package com.enterprise.cartcheckout.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.enterprise.cartcheckout.constants.AppConstants.*;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(EXCHANGE_ORDERS);
    }

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(EXCHANGE_PAYMENTS);
    }

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(EXCHANGE_NOTIFICATIONS);
    }

    @Bean
    public Queue orderQueue() {
        return new Queue(QUEUE_ORDERS, true);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE_NOTIFICATIONS, true);
    }

    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(ROUTING_KEY_ORDER_PLACED);
    }

    @Bean
    public Binding notificationBinding() {
        return BindingBuilder.bind(notificationQueue()).to(notificationExchange()).with(ROUTING_KEY_NOTIFICATION_SEND);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
