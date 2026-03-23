package com.example.MVCOnlineMarketplace.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private long id;
    private long orderId;
    private ProductDto productDto;
    private int quantity;

}
