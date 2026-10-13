package andrei.notificationsservice.service;

import andrei.notificationsservice.documents.EmailLogEntry;
import andrei.notificationsservice.documents.EmailLogRepository;
import andrei.notificationsservice.documents.EventLogEntry;
import andrei.notificationsservice.documents.EventLogRepository;
import andrei.notificationsservice.model.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;
    private final EventLogRepository eventLogRepository;

    public void sendEventNotification(DomainEvent event) {
        log.info("Saving event log for: {}", event.type());
        eventLogRepository.save(EventLogEntry.builder()
                .eventId(event.eventId())
                .type(event.type())
                .entityName(event.entityName())
                .entityId(event.entityId())
                .userEmail(event.userEmail())
                .occurredAt(event.occurredAt())
                .build());
        log.info("Event log saved to MongoDB");

        String userEmail = event.userEmail();
        log.info("Attempting email send to: {}", userEmail);

        String subject = "Event: " + event.type();
        String body = String.format(
                "Event: %s%nEntity: %s (ID: %d)%nOccurred at: %s",
                event.type(), event.entityName(), event.entityId(), event.occurredAt()
        );

        String status;
        String errorMessage = null;
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(userEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            status = "SENT";
        } catch (Exception e) {
            status = "FAILED";
            errorMessage = e.getMessage();
        }

        emailLogRepository.save(EmailLogEntry.builder()
                .to(userEmail)
                .subject(subject)
                .body(body)
                .status(status)
                .errorMessage(errorMessage)
                .sentAt(LocalDateTime.now())
                .build());
    }

    public List<EmailLogEntry> getEmailLogs() {
        return emailLogRepository.findAll();
    }

    public List<EventLogEntry> getEventLogs() {
        return eventLogRepository.findAll();
    }
}
