package com.example.MVCOnlineMarketplace.Export;

import com.example.MVCOnlineMarketplace.Dto.ProductDto;
import com.example.MVCOnlineMarketplace.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ExportService {

    private final ProductService productService;
    private final Map<String, ExportStrategy> strategies;

    @Autowired
    public ExportService(ProductService productService, List<ExportStrategy> strategies) {
        this.productService = productService;
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(ExportStrategy::getFormat, Function.identity()));
    }

    public String exportProducts(String format) {
        List<ProductDto> products = productService.getAllProducts();
        return exportProducts(products, format);
    }

    public String exportProducts(List<ProductDto> products, String format) {
        ExportStrategy strategy = strategies.get(format.toLowerCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown export format: " + format);
        }
        return strategy.export(products);
    }
}
