package com.example.MVCOnlineMarketplace.Controller;

import com.example.MVCOnlineMarketplace.Model.Product;
import com.example.MVCOnlineMarketplace.Service.ProductService;
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

    public List<Product> getProducts(){
        return this.productService.getAllProducts();
    }




}
