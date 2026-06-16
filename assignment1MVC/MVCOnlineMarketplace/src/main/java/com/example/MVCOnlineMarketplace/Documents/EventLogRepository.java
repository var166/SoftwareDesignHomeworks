package com.example.MVCOnlineMarketplace.Documents;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventLogRepository extends MongoRepository<EventLogEntry, String> {}
