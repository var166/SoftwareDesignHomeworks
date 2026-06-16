package andrei.productsservice.service;

import andrei.productsservice.model.Product;
import andrei.productsservice.repository.ProductQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductQueryService implements IProductQueryService {

    private final ProductQueryRepository productQueryRepository;

    @Override
    public List<Product> findAll() {
        return productQueryRepository.findAll();
    }

    @Override
    public List<Product> findAllSorted(String sortBy, String direction) {
        return productQueryRepository.findAll(Sort.by(Sort.Direction.fromString(direction), sortBy));
    }

    @Override
    public Product findById(long id) {
        return productQueryRepository.findById(id);
    }

    @Override
    public Product findByName(String name) {
        return productQueryRepository.findByName(name);
    }

    @Override
    public List<Product> findByShopId(Long shopId) {
        return productQueryRepository.findByShopId(shopId);
    }

    @Override
    public List<Product> findByShopIdSorted(Long shopId, String sortBy, String direction) {
        return productQueryRepository.findByShopId(shopId, Sort.by(Sort.Direction.fromString(direction), sortBy));
    }

    @Override
    public List<Product> findByPriceRange(BigDecimal min, BigDecimal max) {
        return productQueryRepository.findByPriceBetween(min, max);
    }

    @Override
    public List<Product> findByPriceRangeSorted(BigDecimal min, BigDecimal max, String sortBy, String direction) {
        return productQueryRepository.findByPriceBetween(min, max, Sort.by(Sort.Direction.fromString(direction), sortBy));
    }
}
