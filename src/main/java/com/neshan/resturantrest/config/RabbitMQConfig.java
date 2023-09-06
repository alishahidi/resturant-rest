package com.neshan.resturantrest.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RabbitMQConfig {

    ConnectionFactory connectionFactory;

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());

        return template;
    }

    @Bean
    public Declarables createRestaurantDeadSchema() {
        return new Declarables(
                new DirectExchange("x.restaurant.dle"),
                new Queue("q.restaurant.dlq"),
                new Binding("q.restaurant.dlq",
                        Binding.DestinationType.QUEUE, "x.restaurant.dle",
                        "restaurant.dlq",
                        null));
    }

    @Bean
    public Declarables createRestaurantSchema() {
        return new Declarables(
                new TopicExchange("x.restaurant"),
                QueueBuilder.durable("q.restaurant")
                        .withArgument(
                                "x-dead-letter-exchange",
                                "x.restaurant.dle"
                        )
                        .withArgument(
                                "x-dead-letter-routing-key",
                                "restaurant.dlq"
                        )
                        .build(),
                new Binding("q.restaurant",
                        Binding.DestinationType.QUEUE, "x.restaurant",
                        "restaurant.*",
                        null));
    }
}
