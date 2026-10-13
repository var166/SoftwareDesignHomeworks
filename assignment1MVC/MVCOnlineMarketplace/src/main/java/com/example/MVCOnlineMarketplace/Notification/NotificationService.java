package com.example.MVCOnlineMarketplace.Notification;

import com.example.MVCOnlineMarketplace.Documents.EmailLogEntry;
import com.example.MVCOnlineMarketplace.Documents.EmailLogRepository;
import com.example.MVCOnlineMarketplace.Event.DomainEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;

    @Autowired
    public NotificationService(JavaMailSender mailSender, EmailLogRepository emailLogRepository) {
        this.mailSender = mailSender;
        this.emailLogRepository = emailLogRepository;
    }

    public void sendEventNotification(DomainEvent event) {
        String subject = "Marketplace Event: " + event.type();
        String body = String.format(
                "Event: %s%nEntity: %s (ID: %d)%nOccurred at: %s",
                event.type(), event.entityName(), event.entityId(), event.occurredAt()
        );

        String status;
        String errorMessage = null;

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(event.userEmail());
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            status = "SENT";
        } catch (Exception e) {
            status = "FAILED";
            errorMessage = e.getMessage();
            System.err.println("Failed to send notification email to " + event.userEmail() + ": " + e.getMessage());
        }

        emailLogRepository.save(EmailLogEntry.builder()
                .to(event.userEmail())
                .subject(subject)
                .body(body)
                .status(status)
                .errorMessage(errorMessage)
                .sentAt(LocalDateTime.now())
                .build());
    }
    public List<EmailLogEntry> getEmailLogs(){
        return emailLogRepository.findAll();
    }
}
