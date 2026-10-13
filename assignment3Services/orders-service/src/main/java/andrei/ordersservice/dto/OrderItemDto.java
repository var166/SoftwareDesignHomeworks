package andrei.ordersservice.dto;

import andrei.ordersservice.model.OrderItem;

import java.math.BigDecimal;

public class OrderItemDto {
    public record OrderItemRequestDto(Long productId, int quantity, BigDecimal pricePerUnit) {}

    public record OrderItemResponseDto(long id, Long productId, int quantity, BigDecimal pricePerUnit) {
        public static OrderItemResponseDto from(OrderItem item) {
            return new OrderItemResponseDto(item.getId(), item.getProductId(), item.getQuantity(), item.getPricePerUnit());
        }
    }
}
