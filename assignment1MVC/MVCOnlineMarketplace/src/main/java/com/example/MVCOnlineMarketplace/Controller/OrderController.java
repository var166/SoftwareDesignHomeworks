package com.example.MVCOnlineMarketplace.Controller;

import com.example.MVCOnlineMarketplace.Dto.OrderDto;
import com.example.MVCOnlineMarketplace.Service.OrderService;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public List<OrderDto> getAllOrders() { return orderService.getAllOrders(); }
    public List<OrderDto> getOrdersByUserId(long userId) { return orderService.getOrdersByUserId(userId); }
    public Optional<OrderDto> getOrderById(long id) { return orderService.getOrderById(id); }

    // Remember, your OrderService.save() should take a DTO!
    public void saveOrder(OrderDto orderDto) { orderService.save(orderDto); }
    public void deleteOrder(long id) { orderService.deleteById(id); }

    public void updateOrderStatus(long id, boolean isPaid) { orderService.updateOrderStatus(id, isPaid); }
}