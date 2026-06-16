package andrei.productsservice;

import andrei.productsservice.model.Product;
import andrei.productsservice.repository.ProductQueryRepository;
import andrei.productsservice.service.ProductQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductQueryServiceTest {

    @Mock
    private ProductQueryRepository productQueryRepository;
    @InjectMocks
    private ProductQueryService productQueryService;

    @Test
    void findAll_returnsAllProducts() {
        when(productQueryRepository.findAll()).thenReturn(List.of(new Product(), new Product()));

        List<Product> result = productQueryService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void findById_delegatesToRepo() {
        Product product = Product.builder().name("Widget").build();
        when(productQueryRepository.findById(1L)).thenReturn(product);

        Product result = productQueryService.findById(1L);

        assertThat(result.getName()).isEqualTo("Widget");
    }

    @Test
    void findByName_delegatesToRepo() {
        Product product = Product.builder().name("Widget").build();
        when(productQueryRepository.findByName("Widget")).thenReturn(product);

        Product result = productQueryService.findByName("Widget");

        assertThat(result.getName()).isEqualTo("Widget");
    }

    @Test
    void findByShopId_returnsProductsForShop() {
        List<Product> products = List.of(
                Product.builder().shopId(1L).build(),
                Product.builder().shopId(1L).build()
        );
        when(productQueryRepository.findByShopId(1L)).thenReturn(products);

        List<Product> result = productQueryService.findByShopId(1L);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(p -> p.getShopId().equals(1L));
    }
}
