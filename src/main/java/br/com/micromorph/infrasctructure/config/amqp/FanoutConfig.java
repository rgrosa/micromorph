package br.com.micromorph.infrasctructure.config.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutConfig {

    @Autowired
    private Queue dataQueue;

    @Value("${rabbitmq.fanout.exchange.queue}")
    private String fanoutExchangeQueue;

    @Bean
    Exchange fanoutExchange(){
        return ExchangeBuilder
                .fanoutExchange(fanoutExchangeQueue)
                .durable(true)
                .build();
    }

    @Bean
    Binding DataFanoutBind(){
        return BindingBuilder
                .bind(dataQueue)
                .to(fanoutExchange())
                .with("")
                .noargs();
    }
}
