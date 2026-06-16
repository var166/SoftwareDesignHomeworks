package andrei.productsservice.dto;

import andrei.productsservice.model.Product;

import java.math.BigDecimal;

public class ProductDto {
    public record ProductRequestDto(String name, String description, BigDecimal price, Long shopId) {}
    public record ProductResponseDto(long id, String name, String description, BigDecimal price, Long shopId){
        public static ProductResponseDto from(Product product){
            return new ProductResponseDto(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getShopId());
        }
    }
}
