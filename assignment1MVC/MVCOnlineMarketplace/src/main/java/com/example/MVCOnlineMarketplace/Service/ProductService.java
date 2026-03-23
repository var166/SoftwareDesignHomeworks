package com.example.MVCOnlineMarketplace.Service;

import com.example.MVCOnlineMarketplace.Dto.ProductDto;
import com.example.MVCOnlineMarketplace.Model.Product;
import com.example.MVCOnlineMarketplace.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {
    List<ProductDto> getAllProducts();
    void saveProduct(ProductDto productDto);
    void deleteProduct(long id);
    Optional<ProductDto> getProductById(long id);
}
