package com.example.MVCOnlineMarketplace.Service;

import com.example.MVCOnlineMarketplace.Dto.ShopDto;
import com.example.MVCOnlineMarketplace.Model.Shop;

import java.util.stream.Collectors;

public class ShopMapper {
    public static ShopDto mapToShopDto(Shop shop){
        return ShopDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .description(shop.getDescription())
                .address(shop.getAddress())
                .phone(shop.getPhone())
                .email(shop.getEmail())
                .adminId(shop.getAdmin() != null ? shop.getAdmin().getId() : null)

                .products(shop.getProducts() != null ?
                        shop.getProducts().stream().map(ProductMapper::mapToProductDto).collect(Collectors.toList())
                        : null)
                .build();
    }
    public static Shop mapFromShopDto(ShopDto shopDto){
        return Shop.builder().id(shopDto.getId()).name(shopDto.getName()).description(shopDto.getDescription()).address(shopDto.getAddress()).phone(shopDto.getPhone()).email(shopDto.getEmail()).build();
    }
}
