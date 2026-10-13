package andrei.productsservice.repository;

import andrei.productsservice.model.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductQueryRepository extends Repository<Product, Long> {
    List<Product> findAll();
    List<Product> findAll(Sort sort);
    Product findById(long id);
    Product findByName(String name);
    List<Product> findByShopId(Long shopId);
    List<Product> findByShopId(Long shopId, Sort sort);
    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);
    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max, Sort sort);
}
