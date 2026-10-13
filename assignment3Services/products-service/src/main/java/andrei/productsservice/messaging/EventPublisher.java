package andrei.productsservice.messaging;

import andrei.productsservice.config.RabbitMQConfig;
import andrei.productsservice.model.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import java.io.Console;

@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final AmqpTemplate amqpTemplate;

    public void publish(DomainEvent event) {
        amqpTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, event);
        System.out.println("Event published: " + event);
    }
}
