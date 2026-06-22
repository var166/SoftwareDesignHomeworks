package com.example.MVCOnlineMarketplace.Service;

import com.example.MVCOnlineMarketplace.Dto.OrderItemDto;
import com.example.MVCOnlineMarketplace.Model.Order;
import com.example.MVCOnlineMarketplace.Model.OrderItem;
import com.example.MVCOnlineMarketplace.Model.Product;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    public static OrderItemDto mapToDto(OrderItem item) {
        return OrderItemDto.builder()
                .id(item.getId())
                .orderId(item.getOrder().getOrderId())
                .quantity(item.getQuantity())
                .productDto(ProductMapper.mapToProductDto(item.getProduct()))
                .build();
    }

    public static OrderItem mapFromDto(OrderItemDto dto, Order order, Product product) {
        return OrderItem.builder()
                .id(dto.getId())
                .order(order)
                .product(product)
                .quantity(dto.getQuantity())
                .build();
    }
}
