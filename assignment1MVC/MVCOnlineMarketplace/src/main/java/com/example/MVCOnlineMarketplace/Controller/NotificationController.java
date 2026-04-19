package com.example.MVCOnlineMarketplace.Controller;

import com.example.MVCOnlineMarketplace.Event.DomainEvent;
import com.example.MVCOnlineMarketplace.Notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void handleEvent(DomainEvent event) {
        notificationService.sendEventNotification(event);
    }
}
