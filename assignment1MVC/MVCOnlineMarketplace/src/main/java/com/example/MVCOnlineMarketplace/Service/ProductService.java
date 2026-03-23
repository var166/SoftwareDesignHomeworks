package com.example.MVCOnlineMarketplace.Service;

import com.example.MVCOnlineMarketplace.Dto.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductDto> getAllProducts();
    List<ProductDto> getProductsByShopId(long shopId);
    List<ProductDto> getFiltered(Long shopId, String column, String value, String sortBy, boolean ascending);
    void saveProduct(ProductDto productDto);
    void deleteProduct(long id);
    Optional<ProductDto> getProductById(long id);
}
