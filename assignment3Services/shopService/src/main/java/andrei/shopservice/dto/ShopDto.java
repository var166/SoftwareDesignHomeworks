package andrei.shopservice.dto;

import andrei.shopservice.model.Shop;

import java.util.Map;

public class ShopDto {
    public record ShopRequestDto(String name, String address, String phone, String email, String description, Long adminId) {}

    public record ShopResponseDto(long id, String name, String address, String phone, String email, String description, Long adminId, Map<Long, Integer> productStock) {
        public static ShopResponseDto from(Shop shop) {
            return new ShopResponseDto(
                    shop.getId(),
                    shop.getName(),
                    shop.getAddress(),
                    shop.getPhone(),
                    shop.getEmail(),
                    shop.getDescription(),
                    shop.getAdminId(),
                    shop.getProductStock()
            );
        }
    }
}
