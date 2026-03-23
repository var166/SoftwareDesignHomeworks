package com.example.MVCOnlineMarketplace.Service;

import com.example.MVCOnlineMarketplace.Dto.ShopDto;
import java.util.List;
import java.util.Optional;

public interface ShopService {
    List<ShopDto> getAllShops();
    Optional<ShopDto> getShopById(long id);
    ShopDto saveShop(ShopDto shopDto);
    void deleteShop(long id);
}