package andrei.ordersservice;

import andrei.ordersservice.model.Order;
import andrei.ordersservice.model.OrderItem;
import andrei.ordersservice.repository.OrderItemQueryRepository;
import andrei.ordersservice.repository.OrderQueryRepository;
import andrei.ordersservice.service.OrderQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderQueryServiceTest {

    @Mock
    private OrderQueryRepository orderQueryRepository;
    @Mock
    private OrderItemQueryRepository orderItemQueryRepository;
    @InjectMocks
    private OrderQueryService orderQueryService;

    @Test
    void findAll_returnsAllOrders() {
        when(orderQueryRepository.findAll()).thenReturn(List.of(new Order(), new Order()));

        List<Order> result = orderQueryService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void findById_delegatesToRepo() {
        Order order = Order.builder().userId(1L).build();
        when(orderQueryRepository.findByOrderId(1L)).thenReturn(order);

        Order result = orderQueryService.findById(1L);

        assertThat(result.getUserId()).isEqualTo(1L);
    }

    @Test
    void findByUserId_returnsOrdersForUser() {
        List<Order> orders = List.of(Order.builder().userId(1L).build(), Order.builder().userId(1L).build());
        when(orderQueryRepository.findByUserId(1L)).thenReturn(orders);

        List<Order> result = orderQueryService.findByUserId(1L);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(o -> o.getUserId().equals(1L));
    }

    @Test
    void findByIsPaid_returnsMatchingOrders() {
        List<Order> unpaid = List.of(new Order(), new Order(), new Order());
        when(orderQueryRepository.findByIsPaid(false)).thenReturn(unpaid);

        List<Order> result = orderQueryService.findByIsPaid(false);

        assertThat(result).hasSize(3);
    }

    @Test
    void findItemsByOrderId_returnsItemsForOrder() {
        List<OrderItem> items = List.of(new OrderItem(), new OrderItem());
        when(orderItemQueryRepository.findByOrderOrderId(1L)).thenReturn(items);

        List<OrderItem> result = orderQueryService.findItemsByOrderId(1L);

        assertThat(result).hasSize(2);
    }
}
