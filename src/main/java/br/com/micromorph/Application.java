package br.com.micromorph;

import org.springframework.amqp.rabbit.listener.ListenerContainerConsumerFailedEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.UnknownHostException;

@SpringBootApplication
@EnableScheduling
@EnableElasticsearchRepositories
public class Application {

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);

	}


}
