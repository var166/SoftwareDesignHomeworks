package andrei.productsservice.repository;

import andrei.productsservice.model.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

public interface ProductCommandRepository extends Repository<Product, Long> {
    Product save(Product product);
    void deleteById(long id);
    Optional<Product> findById(long id);

    @Modifying
    @Query("UPDATE Product p SET p.price = :price WHERE p.id = :id")
    void updatePrice(@Param("id") long id, @Param("price") BigDecimal price);

    @Modifying
    @Query("UPDATE Product p SET p.description = :description WHERE p.id = :id")
    void updateDescription(@Param("id") long id, @Param("description") String description);
}
