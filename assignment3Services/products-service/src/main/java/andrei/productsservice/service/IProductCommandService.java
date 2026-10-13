package andrei.productsservice.service;

import andrei.productsservice.model.Product;

import java.math.BigDecimal;

public interface IProductCommandService {
    Product create(String name, String description, BigDecimal price, Long shopId, String userEmail);
    boolean updatePrice(long id, BigDecimal price, String userEmail);
    boolean updateDescription(long id, String description, String userEmail);
    boolean delete(long id);
}
