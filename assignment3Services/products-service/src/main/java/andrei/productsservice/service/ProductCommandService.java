package andrei.productsservice.service;

import andrei.productsservice.messaging.EventPublisher;
import andrei.productsservice.model.DomainEvent;
import andrei.productsservice.model.Product;
import andrei.productsservice.repository.ProductCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductCommandService implements IProductCommandService {

    private final ProductCommandRepository productCommandRepository;
    private final EventPublisher eventPublisher;

    @Override
    @Transactional
    public Product create(String name, String description, BigDecimal price, Long shopId, String userEmail) {
        Product saved = productCommandRepository.save(
                Product.builder()
                        .name(name)
                        .description(description)
                        .price(price)
                        .shopId(shopId)
                        .build()
        );
        eventPublisher.publish(new DomainEvent(
                UUID.randomUUID().toString(),
                "PRODUCT_CREATED",
                "Product",
                saved.getId(),
                userEmail,
                LocalDateTime.now()
        ));
        return saved;
    }

    @Override
    @Transactional
    public boolean updatePrice(long id, BigDecimal price, String userEmail) {
        return productCommandRepository.findById(id)
                .map(product -> {
                    productCommandRepository.updatePrice(id, price);
                    eventPublisher.publish(new DomainEvent(
                            UUID.randomUUID().toString(),
                            "PRODUCT_PRICE_UPDATED",
                            "Product",
                            id,
                            userEmail,
                            LocalDateTime.now()
                    ));
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean updateDescription(long id, String description, String userEmail) {
        return productCommandRepository.findById(id)
                .map(product -> {
                    productCommandRepository.updateDescription(id, description);
                    eventPublisher.publish(new DomainEvent(
                            UUID.randomUUID().toString(),
                            "PRODUCT_DESCRIPTION_UPDATED",
                            "Product",
                            id,
                            userEmail,
                            LocalDateTime.now()
                    ));
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean delete(long id) {
        return productCommandRepository.findById(id)
                .map(product -> {
                    productCommandRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
}
