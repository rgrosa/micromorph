package br.com.micromorph.domain.component.consumer;

import br.com.micromorph.domain.dto.MicromorphDataDTO;
import br.com.micromorph.domain.service.DataService;
import br.com.micromorph.infrasctructure.exception.NotSupportedException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataConsumerAmqp {

    @Autowired
    DataService dataService;

    @RabbitListener(queues = "{rabbitmq.data.consumer.key}")
    public void receiveMessageFromQueue(MicromorphDataDTO micromorphData) throws Exception, NotSupportedException {
        dataService.createAndPersistDataObject(micromorphData);
    }
}
