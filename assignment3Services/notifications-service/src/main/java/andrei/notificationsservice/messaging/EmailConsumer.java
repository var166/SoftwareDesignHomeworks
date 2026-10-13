package andrei.notificationsservice.messaging;

import andrei.notificationsservice.config.RabbitMQConfig;
import andrei.notificationsservice.model.DomainEvent;
import andrei.notificationsservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consume(DomainEvent event) {
        log.info("Received event: type={}, entityId={}, userEmail={}", event.type(), event.entityId(), event.userEmail());
        try {
            notificationService.sendEventNotification(event);
            log.info("Event processed successfully: {}", event.eventId());
        } catch (Exception e) {
            log.error("Failed to process event {}: {}", event.eventId(), e.getMessage(), e);
        }
    }
}
