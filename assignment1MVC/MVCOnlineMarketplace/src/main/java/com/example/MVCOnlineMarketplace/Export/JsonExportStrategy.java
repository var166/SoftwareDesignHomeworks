package com.example.MVCOnlineMarketplace.Export;

import com.example.MVCOnlineMarketplace.Dto.ProductDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JsonExportStrategy implements ExportStrategy {

    private final ObjectMapper objectMapper;

    public JsonExportStrategy() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public String export(List<ProductDto> products) {
        try {
            return objectMapper.writeValueAsString(products);
        } catch (Exception e) {
            throw new RuntimeException("Failed to export as JSON", e);
        }
    }

    @Override
    public String getFormat() {
        return "json";
    }
}
