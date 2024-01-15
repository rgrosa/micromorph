package br.com.micromorph.infrasctructure.config.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    @Value("${rabbitmq.data.consumer.key}")
    private String dataQueue;

    @Bean
    Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper){
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    Queue dataQueue(){
        return QueueBuilder
                .durable(dataQueue)
                .build();
    }
}
