package com.example.MVCOnlineMarketplace.Documents;

import com.example.MVCOnlineMarketplace.Dto.ProductDocumentDto;
import com.example.MVCOnlineMarketplace.Dto.ProductDto;

public class ProductDocumentMapper {

    public static ProductDocument fromProductDto(ProductDto dto) {
        return ProductDocument.builder()
                .productId(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .shopId(dto.getShopId())
                .build();
    }

    public static ProductDocumentDto toProductDocumentDto(ProductDocument doc) {
        return ProductDocumentDto.builder()
                .id(doc.getId())
                .productId(doc.getProductId())
                .name(doc.getName())
                .description(doc.getDescription())
                .price(doc.getPrice())
                .shopId(doc.getShopId())
                .build();
    }
}
