package com.example.MVCOnlineMarketplace.Service;

import com.example.MVCOnlineMarketplace.Dto.ProductDto;
import com.example.MVCOnlineMarketplace.Model.Product;
import com.example.MVCOnlineMarketplace.Model.Shop;
import com.example.MVCOnlineMarketplace.Repositories.ProductRepository;
import com.example.MVCOnlineMarketplace.Repositories.ShopRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;

    @Autowired
    public ProductServiceImplementation(ProductRepository productRepository, ShopRepository shopRepository) {
        this.productRepository = productRepository;
        this.shopRepository = shopRepository;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream().map(ProductMapper::mapToProductDto).toList();
    }

    @Override
    public List<ProductDto> getProductsByShopId(long shopId) {
        return productRepository.findByShop_Id(shopId).stream().map(ProductMapper::mapToProductDto).toList();
    }

    @Override
    public List<ProductDto> getFiltered(Long shopId, String column, String value, String sortBy, boolean ascending) {
        List<ProductDto> list = new ArrayList<>(shopId != null ? getProductsByShopId(shopId) : getAllProducts());
        if (column != null && value != null && !value.isBlank()) {
            String lower = value.toLowerCase();
            list = list.stream().filter(p -> {
                if ("name".equals(column)) return p.getName() != null && p.getName().toLowerCase().contains(lower);
                if ("description".equals(column)) return p.getDescription() != null && p.getDescription().toLowerCase().contains(lower);
                if ("price".equals(column)) return p.getPrice() != null && p.getPrice().toPlainString().contains(value);
                if ("shopId".equals(column)) return p.getShopId() != null && p.getShopId().toString().contains(value);
                return true;
            }).collect(Collectors.toList());
        }
        if (sortBy != null && !sortBy.isBlank()) {
            list = list.stream().sorted((a, b) -> {
                int cmp;
                switch (sortBy) {
                    case "name": cmp = nullSafeCompare(a.getName(), b.getName()); break;
                    case "description": cmp = nullSafeCompare(a.getDescription(), b.getDescription()); break;
                    case "price": cmp = a.getPrice() != null && b.getPrice() != null ? a.getPrice().compareTo(b.getPrice()) : 0; break;
                    case "shopId": cmp = a.getShopId() != null && b.getShopId() != null ? a.getShopId().compareTo(b.getShopId()) : 0; break;
                    default: cmp = Long.compare(a.getId(), b.getId());
                }
                return ascending ? cmp : -cmp;
            }).collect(Collectors.toList());
        }
        return list;
    }

    private int nullSafeCompare(String a, String b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareToIgnoreCase(b);
    }

    @Override
    public void saveProduct(ProductDto productDto) {
        Product product = ProductMapper.mapFromProductDto(productDto);
        if (productDto.getShopId() != null) {
            Shop shop = shopRepository.findById(productDto.getShopId())
                    .orElseThrow(() -> new RuntimeException("Shop not found"));
            product.setShop(shop);
        }
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Optional<ProductDto> getProductById(long id) {
        return productRepository.findById(id).map(ProductMapper::mapToProductDto);
    }
}
