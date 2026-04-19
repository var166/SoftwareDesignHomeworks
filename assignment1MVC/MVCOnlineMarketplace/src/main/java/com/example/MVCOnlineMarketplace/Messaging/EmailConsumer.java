package com.example.MVCOnlineMarketplace.Messaging;

import com.example.MVCOnlineMarketplace.Config.RabbitMQConfig;
import com.example.MVCOnlineMarketplace.Controller.NotificationController;
import com.example.MVCOnlineMarketplace.Event.DomainEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    private final NotificationController notificationController;

    @Autowired
    public EmailConsumer(NotificationController notificationController) {
        this.notificationController = notificationController;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consume(DomainEvent event) {
        if (event.userEmail() != null && !event.userEmail().isBlank()) {
            notificationController.handleEvent(event);
        }
    }
}
