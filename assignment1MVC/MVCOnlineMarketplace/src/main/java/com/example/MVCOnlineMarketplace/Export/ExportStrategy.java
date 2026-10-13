package com.example.MVCOnlineMarketplace.Export;

import com.example.MVCOnlineMarketplace.Dto.ProductDto;

import java.util.List;

public interface ExportStrategy {
    String export(List<ProductDto> products);
    String getFormat();
}
