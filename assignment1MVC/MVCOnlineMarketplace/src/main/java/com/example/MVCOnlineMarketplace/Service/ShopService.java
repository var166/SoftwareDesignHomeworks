package com.example.MVCOnlineMarketplace.Service;

import com.example.MVCOnlineMarketplace.Dto.ShopDto;

import java.util.List;
import java.util.Optional;

public interface ShopService {
    List<ShopDto> getAllShops();
    List<ShopDto> getFiltered(String column, String value, String sortBy, boolean ascending);
    Optional<ShopDto> getShopById(long id);
    Optional<ShopDto> getShopByAdminId(long adminId);
    ShopDto saveShop(ShopDto shopDto);
    void deleteShop(long id);
}
