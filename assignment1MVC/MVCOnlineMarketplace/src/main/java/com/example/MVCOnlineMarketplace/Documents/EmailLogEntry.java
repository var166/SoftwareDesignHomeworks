package com.example.MVCOnlineMarketplace.Documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "email_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailLogEntry {
    @Id
    private String id;
    private String to;
    private String subject;
    private String body;
    private String status;
    private String errorMessage;
    private LocalDateTime sentAt;
}
