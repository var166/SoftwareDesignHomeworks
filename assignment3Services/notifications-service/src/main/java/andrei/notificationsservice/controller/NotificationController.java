package andrei.notificationsservice.controller;

import andrei.notificationsservice.documents.EmailLogEntry;
import andrei.notificationsservice.documents.EventLogEntry;
import andrei.notificationsservice.model.DomainEvent;
import andrei.notificationsservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @GetMapping("/email-logs")
    public List<EmailLogEntry> getEmailLogs() {
        return notificationService.getEmailLogs();
    }

    @GetMapping("/event-logs")
    public List<EventLogEntry> getEventLogs() {
        return notificationService.getEventLogs();
    }
}
