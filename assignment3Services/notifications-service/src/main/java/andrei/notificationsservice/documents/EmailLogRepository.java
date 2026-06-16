package andrei.notificationsservice.documents;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EmailLogRepository extends MongoRepository<EmailLogEntry, String> {
    List<EmailLogEntry> findByStatus(String status);
    List<EmailLogEntry> findByTo(String to);
}
