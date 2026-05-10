package andrei.productsservice.service;

import andrei.productsservice.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface IProductQueryService {
    List<Product> findAll();
    List<Product> findAllSorted(String sortBy, String direction);
    Product findById(long id);
    Product findByName(String name);
    List<Product> findByShopId(Long shopId);
    List<Product> findByShopIdSorted(Long shopId, String sortBy, String direction);
    List<Product> findByPriceRange(BigDecimal min, BigDecimal max);
    List<Product> findByPriceRangeSorted(BigDecimal min, BigDecimal max, String sortBy, String direction);
}
