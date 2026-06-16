package com.example.MVCOnlineMarketplace.Controller;

import com.example.MVCOnlineMarketplace.Documents.EmailLogEntry;
import com.example.MVCOnlineMarketplace.Event.DomainEvent;
import com.example.MVCOnlineMarketplace.Notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

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
    public List<EmailLogEntry> getEmailLogs() {
        return notificationService.getEmailLogs();
    }
}
