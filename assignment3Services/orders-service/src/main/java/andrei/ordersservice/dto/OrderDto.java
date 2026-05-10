package andrei.ordersservice.dto;

import andrei.ordersservice.model.Order;

import java.math.BigDecimal;
import java.util.List;

public class OrderDto {
    public record OrderRequestDto(Long userId, List<OrderItemDto.OrderItemRequestDto> items) {}

    public record OrderResponseDto(long orderId, Long userId, BigDecimal totalPrice, boolean isPaid, List<OrderItemDto.OrderItemResponseDto> items) {
        public static OrderResponseDto from(Order order) {
            return new OrderResponseDto(
                    order.getOrderId(),
                    order.getUserId(),
                    order.getTotalPrice(),
                    order.isIsPaid(),
                    order.getOrderItems().stream().map(OrderItemDto.OrderItemResponseDto::from).toList()
            );
        }
    }
}
