package com.example.MVCOnlineMarketplace.Controller;

import com.example.MVCOnlineMarketplace.Dto.ProductDto;
import com.example.MVCOnlineMarketplace.Export.ExportService;
import com.example.MVCOnlineMarketplace.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final ExportService exportService;

    @Autowired
    public ProductController(ProductService productService, ExportService exportService) {
        this.productService = productService;
        this.exportService = exportService;
    }

    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    public List<ProductDto> getProductsByShopId(long shopId) {
        return productService.getProductsByShopId(shopId);
    }

    public List<ProductDto> getFiltered(Long shopId, String column, String value, String sortBy, boolean ascending) {
        return productService.getFiltered(shopId, column, value, sortBy, ascending);
    }

    public void saveProduct(ProductDto productDto) {
        productService.saveProduct(productDto);
    }

    public void deleteProduct(long id) {
        productService.deleteProduct(id);
    }

    public String exportProducts(String format) {
        return exportService.exportProducts(format);
    }

    public String exportProducts(List<ProductDto> products, String format) {
        return exportService.exportProducts(products, format);
    }
}
