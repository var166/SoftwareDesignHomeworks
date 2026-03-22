package com.example.MVCOnlineMarketplace.Dto;

import com.example.MVCOnlineMarketplace.Model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto implements Dto<Order>{
    private long id;
    private long userId;


}
