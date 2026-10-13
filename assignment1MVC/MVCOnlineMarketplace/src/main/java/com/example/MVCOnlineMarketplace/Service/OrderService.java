package com.example.MVCOnlineMarketplace.Service;

import com.example.MVCOnlineMarketplace.Dto.OrderDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Optional<OrderDto> getOrderById(long id);
    void save(OrderDto order);
    void deleteById(long id);
    boolean existsById(long id);
    List<OrderDto> getAllOrders();
    List<OrderDto> getOrdersByUserId(long userId);
    List<OrderDto> getFilteredByUserId(long userId, String column, String value, String sortBy, boolean ascending);
    void updateOrderStatus(long orderId, boolean isPaid);
}
