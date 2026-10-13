package com.example.MVCOnlineMarketplace.Documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "event_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventLogEntry {
    @Id
    private String id;
    private String eventId;
    private String type;
    private String entityName;
    private Long entityId;
    private String userEmail;
    private LocalDateTime occurredAt;
}
