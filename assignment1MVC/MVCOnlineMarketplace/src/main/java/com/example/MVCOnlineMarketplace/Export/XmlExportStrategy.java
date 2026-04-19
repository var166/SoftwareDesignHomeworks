package com.example.MVCOnlineMarketplace.Export;

import com.example.MVCOnlineMarketplace.Dto.ProductDto;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class XmlExportStrategy implements ExportStrategy {

    private final XmlMapper xmlMapper;

    public XmlExportStrategy() {
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.registerModule(new JavaTimeModule());
        this.xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.xmlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public String export(List<ProductDto> products) {
        try {
            return xmlMapper.writeValueAsString(new ProductListWrapper(products));
        } catch (Exception e) {
            throw new RuntimeException("Failed to export as XML", e);
        }
    }

    @Override
    public String getFormat() {
        return "xml";
    }

    @JacksonXmlRootElement(localName = "products")
    private static class ProductListWrapper {
        @JacksonXmlProperty(localName = "product")
        @JacksonXmlElementWrapper(useWrapping = false)
        public final List<ProductDto> product;

        ProductListWrapper(List<ProductDto> product) {
            this.product = product;
        }
    }
}
