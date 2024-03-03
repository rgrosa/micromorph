package br.com.micromorph.domain.component.consumer;

import br.com.micromorph.domain.service.DataService;
import br.com.micromorph.infrasctructure.exception.NotSupportedException;
import br.com.micromorph.infrasctructure.exception.PersistenceDeserializationException;
import br.com.micromorph.infrasctructure.util.PayloadConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class DataConsumerAmqp {

    @Autowired
    DataService dataService;

    private Logger logger = LoggerFactory.getLogger(DataConsumerAmqp.class);

    @RabbitListener(queues = "${rabbitmq.data.consumer.key}")
    public void receiveMessageFromQueue(Message payload) throws Exception, NotSupportedException {
        try{
            dataService.createAndPersistDataObject(
                    PayloadConverter.convertJsonToDto(
                            new String(payload.getBody(), StandardCharsets.UTF_8)
                    )
            );
        }catch (PersistenceDeserializationException Ex){
            logger.info("Payload processed my rabbitMQ with success message properties = {} ", payload.getMessageProperties());
        }
    }
}