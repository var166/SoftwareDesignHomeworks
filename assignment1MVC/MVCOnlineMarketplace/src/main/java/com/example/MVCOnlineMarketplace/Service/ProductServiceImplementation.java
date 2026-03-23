package com.example.MVCOnlineMarketplace.Service;

import com.example.MVCOnlineMarketplace.Dto.ProductDto;
import com.example.MVCOnlineMarketplace.Model.Product;
import com.example.MVCOnlineMarketplace.Model.Shop;
import com.example.MVCOnlineMarketplace.Repositories.ProductRepository;
import com.example.MVCOnlineMarketplace.Repositories.ShopRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImplementation implements ProductService{


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
