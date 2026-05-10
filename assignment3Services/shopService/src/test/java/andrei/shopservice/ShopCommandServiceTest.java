package andrei.shopservice;

import andrei.shopservice.model.Shop;
import andrei.shopservice.repository.ShopCommandRepository;
import andrei.shopservice.service.ShopCommandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopCommandServiceTest {

    @Mock
    private ShopCommandRepository shopCommandRepository;
    @InjectMocks
    private ShopCommandService shopCommandService;

    private Shop shopWithStock(long productId, int stock) {
        HashMap<Long, Integer> map = new HashMap<>();
        map.put(productId, stock);
        return Shop.builder().name("Test").productStock(map).build();
    }

    private Shop emptyShop() {
        return Shop.builder().name("Test").build();
    }

    @Test
    void create_savesAndReturnsShop() {
        Shop saved = Shop.builder().name("My Shop").build();
        when(shopCommandRepository.save(any(Shop.class))).thenReturn(saved);

        Shop result = shopCommandService.create("My Shop", "addr", "phone", "email", "desc", 1L);

        verify(shopCommandRepository).save(any(Shop.class));
        assertThat(result.getName()).isEqualTo("My Shop");
    }

    @Test
    void updateName_shopExists_updatesAndReturnsTrue() {
        when(shopCommandRepository.findById(1L)).thenReturn(Optional.of(emptyShop()));

        boolean result = shopCommandService.updateName(1L, "New Name");

        verify(shopCommandRepository).updateName(1L, "New Name");
        assertThat(result).isTrue();
    }

    @Test
    void updateName_shopNotFound_returnsFalse() {
        when(shopCommandRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = shopCommandService.updateName(99L, "New Name");

        verify(shopCommandRepository, never()).updateName(anyLong(), anyString());
        assertThat(result).isFalse();
    }

    @Test
    void delete_shopExists_deletesAndReturnsTrue() {
        when(shopCommandRepository.findById(1L)).thenReturn(Optional.of(emptyShop()));

        boolean result = shopCommandService.delete(1L);

        verify(shopCommandRepository).deleteById(1L);
        assertThat(result).isTrue();
    }

    @Test
    void delete_shopNotFound_returnsFalse() {
        when(shopCommandRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = shopCommandService.delete(99L);

        verify(shopCommandRepository, never()).deleteById(anyLong());
        assertThat(result).isFalse();
    }

    @Test
    void addProduct_shopExists_addsProductAndReturnsTrue() {
        Shop shop = emptyShop();
        when(shopCommandRepository.findById(1L)).thenReturn(Optional.of(shop));
        when(shopCommandRepository.save(any(Shop.class))).thenReturn(shop);

        boolean result = shopCommandService.addProduct(1L, 5L, 100);

        assertThat(result).isTrue();
        assertThat(shop.getProductStock()).containsEntry(5L, 100);
        verify(shopCommandRepository).save(shop);
    }

    @Test
    void addProduct_productAlreadyExists_doesNotOverwrite() {
        Shop shop = shopWithStock(5L, 50);
        when(shopCommandRepository.findById(1L)).thenReturn(Optional.of(shop));
        when(shopCommandRepository.save(any(Shop.class))).thenReturn(shop);

        shopCommandService.addProduct(1L, 5L, 999);

        assertThat(shop.getProductStock().get(5L)).isEqualTo(50);
    }

    @Test
    void addProduct_shopNotFound_returnsFalse() {
        when(shopCommandRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = shopCommandService.addProduct(99L, 5L, 10);

        assertThat(result).isFalse();
        verify(shopCommandRepository, never()).save(any());
    }

    @Test
    void removeProduct_shopExists_removesAndReturnsTrue() {
        Shop shop = shopWithStock(5L, 50);
        when(shopCommandRepository.findById(1L)).thenReturn(Optional.of(shop));
        when(shopCommandRepository.save(any(Shop.class))).thenReturn(shop);

        boolean result = shopCommandService.removeProduct(1L, 5L);

        assertThat(result).isTrue();
        assertThat(shop.getProductStock()).doesNotContainKey(5L);
    }

    @Test
    void updateStock_productExists_updatesAndReturnsTrue() {
        Shop shop = shopWithStock(5L, 50);
        when(shopCommandRepository.findById(1L)).thenReturn(Optional.of(shop));
        when(shopCommandRepository.save(any(Shop.class))).thenReturn(shop);

        boolean result = shopCommandService.updateStock(1L, 5L, 200);

        assertThat(result).isTrue();
        assertThat(shop.getProductStock().get(5L)).isEqualTo(200);
    }

    @Test
    void updateStock_productNotInShop_returnsFalse() {
        Shop shop = emptyShop();
        when(shopCommandRepository.findById(1L)).thenReturn(Optional.of(shop));

        boolean result = shopCommandService.updateStock(1L, 5L, 200);

        assertThat(result).isFalse();
        verify(shopCommandRepository, never()).save(any());
    }

    @Test
    void incrementStock_addsAmount() {
        Shop shop = shopWithStock(5L, 50);
        when(shopCommandRepository.findById(1L)).thenReturn(Optional.of(shop));
        when(shopCommandRepository.save(any(Shop.class))).thenReturn(shop);

        boolean result = shopCommandService.incrementStock(1L, 5L, 20);

        assertThat(result).isTrue();
        assertThat(shop.getProductStock().get(5L)).isEqualTo(70);
    }

    @Test
    void decrementStock_sufficientStock_subtractsAndReturnsTrue() {
        Shop shop = shopWithStock(5L, 50);
        when(shopCommandRepository.findById(1L)).thenReturn(Optional.of(shop));
        when(shopCommandRepository.save(any(Shop.class))).thenReturn(shop);

        boolean result = shopCommandService.decrementStock(1L, 5L, 20);

        assertThat(result).isTrue();
        assertThat(shop.getProductStock().get(5L)).isEqualTo(30);
    }

    @Test
    void decrementStock_insufficientStock_returnsFalse() {
        Shop shop = shopWithStock(5L, 10);
        when(shopCommandRepository.findById(1L)).thenReturn(Optional.of(shop));

        boolean result = shopCommandService.decrementStock(1L, 5L, 50);

        assertThat(result).isFalse();
        assertThat(shop.getProductStock().get(5L)).isEqualTo(10);
        verify(shopCommandRepository, never()).save(any());
    }
}
