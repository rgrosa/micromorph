package br.com.micromorph.infrasctructure.config.amqp;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    private Logger logger = LoggerFactory.getLogger(RabbitMqConfig.class);

    @Value("${rabbitmq.host}")
    private String host;
    @Value("${rabbitmq.username}")
    private String user;
    @Value("${rabbitmq.password}")
    private String password;
    @Value("${rabbitmq.port}")
    private Integer port;

    @Bean
    public ConnectionFactory createConnectionFactory(){
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(host);
        factory.setUsername(user);
        factory.setPassword(password);
        factory.setPort(port);
        logger.info("rabbitmq config: host:{}, user:{}, port:{}", host, user, port);
        return factory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(createConnectionFactory());
    }

}