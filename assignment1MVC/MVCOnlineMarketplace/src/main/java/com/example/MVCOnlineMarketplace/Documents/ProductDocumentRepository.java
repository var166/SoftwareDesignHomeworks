package com.example.MVCOnlineMarketplace.Documents;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductDocumentRepository extends MongoRepository<ProductDocument, String> {
    Optional<ProductDocument> findByProductId(Long productId);
    void deleteByProductId(Long productId);
}
