package com.example.MVCOnlineMarketplace.Service;

import com.example.MVCOnlineMarketplace.Dto.ShopDto;
import com.example.MVCOnlineMarketplace.Model.Shop;
import com.example.MVCOnlineMarketplace.Model.User;
import com.example.MVCOnlineMarketplace.Repositories.ShopRepository;
import com.example.MVCOnlineMarketplace.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShopServiceImplementation implements ShopService {
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    public ShopServiceImplementation(ShopRepository shopRepository, UserRepository userRepository) {
        this.shopRepository = shopRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ShopDto> getAllShops() {
        return shopRepository.findAll().stream()
                .map(ShopMapper::mapToShopDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ShopDto> getShopById(long id) {
        return shopRepository.findById(id).map(ShopMapper::mapToShopDto);
    }

    @Override
    public ShopDto saveShop(ShopDto shopDto) {
        Shop shop = ShopMapper.mapFromShopDto(shopDto);

        // Link the Admin entity based on the DTO's ID
        if (shopDto.getAdminId() != null) {
            User admin = userRepository.findById(shopDto.getAdminId())
                    .orElseThrow(() -> new RuntimeException("Admin user not found"));
            shop.setAdmin(admin);
        }

        Shop savedShop = shopRepository.save(shop);
        return ShopMapper.mapToShopDto(savedShop);
    }

    @Override
    public void deleteShop(long id) {
        shopRepository.deleteById(id);
    }
}