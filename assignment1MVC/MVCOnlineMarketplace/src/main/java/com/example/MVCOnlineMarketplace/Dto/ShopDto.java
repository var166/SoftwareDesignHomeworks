package com.example.MVCOnlineMarketplace.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopDto {
    private long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String description;
    private Long adminId;
    private List<ProductDto> products;
}
