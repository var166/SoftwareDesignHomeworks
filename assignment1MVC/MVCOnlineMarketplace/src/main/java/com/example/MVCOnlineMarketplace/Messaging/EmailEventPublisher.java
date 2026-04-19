package com.example.MVCOnlineMarketplace.Messaging;

import com.example.MVCOnlineMarketplace.Config.RabbitMQConfig;
import com.example.MVCOnlineMarketplace.Documents.EventLogEntry;
import com.example.MVCOnlineMarketplace.Documents.EventLogRepository;
import com.example.MVCOnlineMarketplace.Event.DomainEvent;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailEventPublisher {

    private final AmqpTemplate amqpTemplate;
    private final EventLogRepository eventLogRepository;

    @Autowired
    public EmailEventPublisher(AmqpTemplate amqpTemplate, EventLogRepository eventLogRepository) {
        this.amqpTemplate = amqpTemplate;
        this.eventLogRepository = eventLogRepository;
    }

    public void publish(DomainEvent event) {
        eventLogRepository.save(EventLogEntry.builder()
                .eventId(event.eventId())
                .type(event.type())
                .entityName(event.entityName())
                .entityId(event.entityId())
                .userEmail(event.userEmail())
                .occurredAt(event.occurredAt())
                .build());

        // Default exchange: route directly to queue by queue name
        amqpTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, event);
    }
}
