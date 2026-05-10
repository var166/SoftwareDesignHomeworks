package andrei.ordersservice.service;

import andrei.ordersservice.dto.OrderItemDto;
import andrei.ordersservice.model.Order;
import andrei.ordersservice.model.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public interface IOrderCommandService {
    Order createOrder(Long userId, List<OrderItemDto.OrderItemRequestDto> items);
    boolean markAsPaid(long orderId);
    boolean deleteOrder(long orderId);
    OrderItem addItem(long orderId, Long productId, int quantity, BigDecimal pricePerUnit);
    boolean removeItem(long itemId);
}
