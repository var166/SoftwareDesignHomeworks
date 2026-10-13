package com.example.MVCOnlineMarketplace.Documents;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailLogRepository extends MongoRepository<EmailLogEntry, String> {}
