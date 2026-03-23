package com.example.MVCOnlineMarketplace.Service;

import com.example.MVCOnlineMarketplace.Dto.ShopDto;
import com.example.MVCOnlineMarketplace.Model.Shop;
import com.example.MVCOnlineMarketplace.Model.User;
import com.example.MVCOnlineMarketplace.Model.UserRole;
import com.example.MVCOnlineMarketplace.Repositories.ShopRepository;
import com.example.MVCOnlineMarketplace.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<ShopDto> getFiltered(String column, String value, String sortBy, boolean ascending) {
        List<ShopDto> list = new ArrayList<>(getAllShops());
        if (column != null && value != null && !value.isBlank()) {
            String lower = value.toLowerCase();
            list = list.stream().filter(s -> {
                if ("name".equals(column)) return s.getName() != null && s.getName().toLowerCase().contains(lower);
                if ("address".equals(column)) return s.getAddress() != null && s.getAddress().toLowerCase().contains(lower);
                if ("phone".equals(column)) return s.getPhone() != null && s.getPhone().toLowerCase().contains(lower);
                if ("email".equals(column)) return s.getEmail() != null && s.getEmail().toLowerCase().contains(lower);
                if ("description".equals(column)) return s.getDescription() != null && s.getDescription().toLowerCase().contains(lower);
                if ("adminId".equals(column)) return s.getAdminId() != null && s.getAdminId().toString().contains(value);
                return true;
            }).collect(Collectors.toList());
        }
        if (sortBy != null && !sortBy.isBlank()) {
            list = list.stream().sorted((a, b) -> {
                int cmp;
                switch (sortBy) {
                    case "name": cmp = nullSafeCompare(a.getName(), b.getName()); break;
                    case "address": cmp = nullSafeCompare(a.getAddress(), b.getAddress()); break;
                    case "phone": cmp = nullSafeCompare(a.getPhone(), b.getPhone()); break;
                    case "email": cmp = nullSafeCompare(a.getEmail(), b.getEmail()); break;
                    case "description": cmp = nullSafeCompare(a.getDescription(), b.getDescription()); break;
                    case "adminId": cmp = a.getAdminId() != null && b.getAdminId() != null ? a.getAdminId().compareTo(b.getAdminId()) : 0; break;
                    default: cmp = Long.compare(a.getId(), b.getId());
                }
                return ascending ? cmp : -cmp;
            }).collect(Collectors.toList());
        }
        return list;
    }

    private int nullSafeCompare(String a, String b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareToIgnoreCase(b);
    }

    @Override
    public Optional<ShopDto> getShopById(long id) {
        return shopRepository.findById(id).map(ShopMapper::mapToShopDto);
    }

    @Override
    public Optional<ShopDto> getShopByAdminId(long adminId) {
        return shopRepository.findByAdmin_Id(adminId).map(ShopMapper::mapToShopDto);
    }

    @Override
    public ShopDto saveShop(ShopDto shopDto) {
        Shop shop;

        if (shopDto.getId() > 0) {
            shop = shopRepository.findById(shopDto.getId())
                    .orElseThrow(() -> new RuntimeException("Shop not found"));

            if (shop.getAdmin() != null) {
                Long oldAdminId = shop.getAdmin().getId();
                boolean adminChanged = shopDto.getAdminId() == null || !oldAdminId.equals(shopDto.getAdminId());
                if (adminChanged) {
                    User oldAdmin = userRepository.findById(oldAdminId).orElse(null);
                    if (oldAdmin != null) {
                        oldAdmin.setUserRole(UserRole.User);
                        userRepository.save(oldAdmin);
                    }
                }
            }

            shop.setName(shopDto.getName());
            shop.setAddress(shopDto.getAddress());
            shop.setPhone(shopDto.getPhone());
            shop.setEmail(shopDto.getEmail());
            shop.setDescription(shopDto.getDescription());
        } else {
            shop = ShopMapper.mapFromShopDto(shopDto);
        }

        if (shopDto.getAdminId() != null) {
            User admin = userRepository.findById(shopDto.getAdminId())
                    .orElseThrow(() -> new RuntimeException("Admin user not found"));
            admin.setUserRole(UserRole.StoreManager);
            userRepository.save(admin);
            shop.setAdmin(admin);
        } else {
            shop.setAdmin(null);
        }

        Shop savedShop = shopRepository.save(shop);
        return ShopMapper.mapToShopDto(savedShop);
    }

    @Override
    public void deleteShop(long id) {
        shopRepository.findById(id).ifPresent(shop -> {
            if (shop.getAdmin() != null) {
                User admin = userRepository.findById(shop.getAdmin().getId()).orElse(null);
                if (admin != null) {
                    admin.setUserRole(UserRole.User);
                    userRepository.save(admin);
                }
            }
        });
        shopRepository.deleteById(id);
    }
}
