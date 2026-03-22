package com.example.MVCOnlineMarketplace.Dto;

import com.example.MVCOnlineMarketplace.Model.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto implements Dto<Product>{
    private long id;
    private String name;
    private String description;
    private double price;
    private int stock;
}


