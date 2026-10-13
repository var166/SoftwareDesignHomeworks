package andrei.productsservice;

import andrei.productsservice.model.Product;
import andrei.productsservice.repository.ProductCommandRepository;
import andrei.productsservice.service.ProductCommandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductCommandServiceTest {

    @Mock
    private ProductCommandRepository productCommandRepository;
    @InjectMocks
    private ProductCommandService productCommandService;

    @Test
    void create_savesAndReturnsProduct() {
        Product saved = Product.builder().name("Widget").description("desc").price(new BigDecimal("9.99")).shopId(1L).build();
        when(productCommandRepository.save(any(Product.class))).thenReturn(saved);

        Product result = productCommandService.create("Widget", "desc", new BigDecimal("9.99"), 1L, "test@test.com");

        verify(productCommandRepository).save(any(Product.class));
        assertThat(result.getName()).isEqualTo("Widget");
        assertThat(result.getPrice()).isEqualByComparingTo("9.99");
    }

    @Test
    void updatePrice_productExists_updatesAndReturnsTrue() {
        when(productCommandRepository.findById(1L)).thenReturn(Optional.of(new Product()));

        boolean result = productCommandService.updatePrice(1L, new BigDecimal("14.99"), "test@test.com");

        verify(productCommandRepository).updatePrice(1L, new BigDecimal("14.99"));
        assertThat(result).isTrue();
    }

    @Test
    void updatePrice_productNotFound_returnsFalse() {
        when(productCommandRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = productCommandService.updatePrice(99L, new BigDecimal("14.99"),"test@test.com");

        verify(productCommandRepository, never()).updatePrice(anyLong(), any());
        assertThat(result).isFalse();
    }

    @Test
    void updateDescription_productExists_updatesAndReturnsTrue() {
        when(productCommandRepository.findById(1L)).thenReturn(Optional.of(new Product()));

        boolean result = productCommandService.updateDescription(1L, "new desc","test@test.com");

        verify(productCommandRepository).updateDescription(1L, "new desc");
        assertThat(result).isTrue();
    }

    @Test
    void updateDescription_productNotFound_returnsFalse() {
        when(productCommandRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = productCommandService.updateDescription(99L, "new desc","test@test.com");

        verify(productCommandRepository, never()).updateDescription(anyLong(), anyString());
        assertThat(result).isFalse();
    }

    @Test
    void delete_productExists_deletesAndReturnsTrue() {
        when(productCommandRepository.findById(1L)).thenReturn(Optional.of(new Product()));

        boolean result = productCommandService.delete(1L);

        verify(productCommandRepository).deleteById(1L);
        assertThat(result).isTrue();
    }

    @Test
    void delete_productNotFound_returnsFalse() {
        when(productCommandRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = productCommandService.delete(99L);

        verify(productCommandRepository, never()).deleteById(anyLong());
        assertThat(result).isFalse();
    }
}
