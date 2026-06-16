package andrei.notificationsservice.model;

import java.time.LocalDateTime;

public record DomainEvent(
        String eventId,
        String type,
        String entityName,
        Long entityId,
        String userEmail,
        LocalDateTime occurredAt
) {}
