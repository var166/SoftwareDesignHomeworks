package com.example.MVCOnlineMarketplace.Service;

import com.example.MVCOnlineMarketplace.Dto.ProductDto;
import com.example.MVCOnlineMarketplace.Model.Product;
import org.springframework.stereotype.Component;


@Component
public class ProductMapper {
    public static ProductDto mapToProductDto(Product product){
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .shopId(product.getShop() != null ? product.getShop().getId() : null)
                .build();
    }
    public static Product mapFromProductDto(ProductDto productDto){
        return Product.builder().id(productDto.getId())
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())

                .build();
    }
}
