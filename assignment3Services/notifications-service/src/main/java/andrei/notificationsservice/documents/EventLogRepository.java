package andrei.notificationsservice.documents;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventLogRepository extends MongoRepository<EventLogEntry, String> {
}
