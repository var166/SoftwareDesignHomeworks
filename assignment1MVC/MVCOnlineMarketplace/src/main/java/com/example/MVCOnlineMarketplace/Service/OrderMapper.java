package com.example.MVCOnlineMarketplace.Service;

import com.example.MVCOnlineMarketplace.Dto.OrderDto;
import com.example.MVCOnlineMarketplace.Model.Order;
import com.example.MVCOnlineMarketplace.Model.OrderItem;
import com.example.MVCOnlineMarketplace.Model.Product;
import com.example.MVCOnlineMarketplace.Model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    public static OrderDto mapToDto(Order order) {
        return OrderDto.builder()
                .id(order.getOrderId())
                .userDto(UserMapper.mapToUserDto(order.getUser()))
                .totalPrice(order.getTotalPrice())
                .orderItems(order.getOrderItems()
                        .stream()
                        .map(OrderItemMapper::mapToDto)
                        .collect(Collectors.toList()))
                .isPaid(order.isPaid())
                .build();
    }

    public static Order mapFromDto(OrderDto dto, Map<Long, Product> productsById, User user) {
        Order order = Order.builder()
                .orderId(dto.getId())
                .user(user)
                .totalPrice(dto.getTotalPrice())
                .isPaid(dto.isPaid())
                .build();

        List<OrderItem> items = dto.getOrderItems().stream()
                .map(itemDto -> OrderItemMapper.mapFromDto(
                        itemDto,
                        order,
                        productsById.get(itemDto.getProductDto().getId())
                ))
                .collect(Collectors.toList());

        order.setOrderItems(items);
        return order;
    }
}
