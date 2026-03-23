package com.example.MVCOnlineMarketplace.Controller;

import com.example.MVCOnlineMarketplace.Dto.ShopDto;
import com.example.MVCOnlineMarketplace.Service.ShopService;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class ShopController {
    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    public List<ShopDto> getAllShops() { return shopService.getAllShops(); }
    public Optional<ShopDto> getShopById(long id) { return shopService.getShopById(id); }
    public ShopDto saveShop(ShopDto shopDto) { return shopService.saveShop(shopDto); }
    public void deleteShop(long id) { shopService.deleteShop(id); }
}