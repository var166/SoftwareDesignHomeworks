package com.example.MVCOnlineMarketplace.Service;

import com.example.MVCOnlineMarketplace.Dto.OrderDto;
import com.example.MVCOnlineMarketplace.Model.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderService {
    public Optional<OrderDto> getOrderById(long id);
    public void save(OrderDto order);

    public void deleteById(long id);
    public boolean existsById(long id);
    public List<OrderDto> getAllOrders();
    public List<OrderDto> getOrdersByUserId(long userId);
    public void updateOrderStatus(long orderId, boolean isPaid);

}
