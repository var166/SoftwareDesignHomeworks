package andrei.shopservice;

import andrei.shopservice.model.Shop;
import andrei.shopservice.repository.ShopQueryRepository;
import andrei.shopservice.service.ShopQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShopQueryServiceTest {

    @Mock
    private ShopQueryRepository shopQueryRepository;
    @InjectMocks
    private ShopQueryService shopQueryService;

    @Test
    void findAll_returnsAllShops() {
        when(shopQueryRepository.findAll()).thenReturn(List.of(new Shop(), new Shop()));

        List<Shop> result = shopQueryService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void findById_delegatesToRepo() {
        Shop shop = Shop.builder().name("My Shop").build();
        when(shopQueryRepository.findById(1L)).thenReturn(shop);

        Shop result = shopQueryService.findById(1L);

        assertThat(result.getName()).isEqualTo("My Shop");
    }

    @Test
    void findByName_delegatesToRepo() {
        Shop shop = Shop.builder().name("My Shop").build();
        when(shopQueryRepository.findByName("My Shop")).thenReturn(shop);

        Shop result = shopQueryService.findByName("My Shop");

        assertThat(result.getName()).isEqualTo("My Shop");
    }

    @Test
    void findByEmail_delegatesToRepo() {
        Shop shop = Shop.builder().email("shop@example.com").build();
        when(shopQueryRepository.findByEmail("shop@example.com")).thenReturn(shop);

        Shop result = shopQueryService.findByEmail("shop@example.com");

        assertThat(result.getEmail()).isEqualTo("shop@example.com");
    }

    @Test
    void findByAdminId_returnsShopsForAdmin() {
        List<Shop> shops = List.of(Shop.builder().adminId(1L).build(), Shop.builder().adminId(1L).build());
        when(shopQueryRepository.findByAdminId(1L)).thenReturn(shops);

        List<Shop> result = shopQueryService.findByAdminId(1L);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(s -> s.getAdminId().equals(1L));
    }
}
