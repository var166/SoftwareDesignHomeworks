package com.example.MVCOnlineMarketplace.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private long id;
    private UserDto userDto;
    private BigDecimal totalPrice;
    private List<OrderItemDto> orderItems;
    private boolean isPaid;
}
