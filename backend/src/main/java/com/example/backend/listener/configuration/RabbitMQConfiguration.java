package com.example.backend.listener.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    @Bean
    Queue queue() {
        return new Queue("fileQueue", false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("fileExchange");
    }

    @Bean
    Binding bindingQueueToExchange(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("fileCreation");
    }
}
