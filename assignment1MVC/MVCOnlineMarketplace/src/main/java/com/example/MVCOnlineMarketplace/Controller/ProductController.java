package com.example.MVCOnlineMarketplace.Controller;

import com.example.MVCOnlineMarketplace.Dto.ProductDto;
import com.example.MVCOnlineMarketplace.Model.Product;
import com.example.MVCOnlineMarketplace.Service.ProductService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Comparator;
import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    public List<ProductDto> getAllProducts() { return productService.getAllProducts(); }
    public void saveProduct(ProductDto productDto) { productService.saveProduct(productDto); }
    public void deleteProduct(long id) { productService.deleteProduct(id); }

}
