package andrei.ordersservice;

import andrei.ordersservice.dto.OrderItemDto;
import andrei.ordersservice.model.Order;
import andrei.ordersservice.model.OrderItem;
import andrei.ordersservice.repository.OrderCommandRepository;
import andrei.ordersservice.repository.OrderItemCommandRepository;
import andrei.ordersservice.service.OrderCommandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderCommandServiceTest {

    @Mock
    private OrderCommandRepository orderCommandRepository;
    @Mock
    private OrderItemCommandRepository orderItemCommandRepository;
    @InjectMocks
    private OrderCommandService orderCommandService;

    @Test
    void createOrder_calculatesTotalAndSaves() {
        List<OrderItemDto.OrderItemRequestDto> items = List.of(
                new OrderItemDto.OrderItemRequestDto(1L, 2, new BigDecimal("10.00")),
                new OrderItemDto.OrderItemRequestDto(2L, 3, new BigDecimal("5.00"))
        );
        when(orderCommandRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderCommandService.createOrder(1L, items);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderCommandRepository).save(captor.capture());
        Order saved = captor.getValue();
        assertThat(saved.getTotalPrice()).isEqualByComparingTo("35.00");
        assertThat(saved.getOrderItems()).hasSize(2);
        assertThat(saved.isIsPaid()).isFalse();
        assertThat(saved.getUserId()).isEqualTo(1L);
    }

    @Test
    void createOrder_eachItemLinkedToOrder() {
        List<OrderItemDto.OrderItemRequestDto> items = List.of(
                new OrderItemDto.OrderItemRequestDto(1L, 1, new BigDecimal("10.00"))
        );
        when(orderCommandRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderCommandService.createOrder(1L, items);

        assertThat(result.getOrderItems().get(0).getOrder()).isSameAs(result);
    }

    @Test
    void markAsPaid_orderExists_updatesAndReturnsTrue() {
        when(orderCommandRepository.findById(1L)).thenReturn(Optional.of(new Order()));

        boolean result = orderCommandService.markAsPaid(1L);

        verify(orderCommandRepository).updateIsPaid(1L, true);
        assertThat(result).isTrue();
    }

    @Test
    void markAsPaid_orderNotFound_returnsFalse() {
        when(orderCommandRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = orderCommandService.markAsPaid(99L);

        verify(orderCommandRepository, never()).updateIsPaid(anyLong(), anyBoolean());
        assertThat(result).isFalse();
    }

    @Test
    void deleteOrder_orderExists_deletesAndReturnsTrue() {
        when(orderCommandRepository.findById(1L)).thenReturn(Optional.of(new Order()));

        boolean result = orderCommandService.deleteOrder(1L);

        verify(orderCommandRepository).deleteById(1L);
        assertThat(result).isTrue();
    }

    @Test
    void deleteOrder_orderNotFound_returnsFalse() {
        when(orderCommandRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = orderCommandService.deleteOrder(99L);

        verify(orderCommandRepository, never()).deleteById(anyLong());
        assertThat(result).isFalse();
    }

    @Test
    void addItem_orderExists_savesItemAndUpdatesTotalPrice() {
        Order order = Order.builder().userId(1L).isPaid(false).totalPrice(new BigDecimal("20.00")).orderItems(new ArrayList<>()).build();
        when(orderCommandRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemCommandRepository.save(any(OrderItem.class))).thenAnswer(inv -> inv.getArgument(0));
        when(orderCommandRepository.save(any(Order.class))).thenReturn(order);

        OrderItem result = orderCommandService.addItem(1L, 3L, 2, new BigDecimal("5.00"));

        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(3L);
        assertThat(result.getQuantity()).isEqualTo(2);
        assertThat(order.getTotalPrice()).isEqualByComparingTo("30.00");
    }

    @Test
    void addItem_orderNotFound_returnsNull() {
        when(orderCommandRepository.findById(99L)).thenReturn(Optional.empty());

        OrderItem result = orderCommandService.addItem(99L, 1L, 1, new BigDecimal("5.00"));

        assertThat(result).isNull();
        verify(orderItemCommandRepository, never()).save(any());
    }

    @Test
    void removeItem_itemExists_deductsTotalAndDeletes() {
        Order order = Order.builder().totalPrice(new BigDecimal("30.00")).orderItems(new ArrayList<>()).build();
        OrderItem item = OrderItem.builder().order(order).productId(1L).quantity(2).pricePerUnit(new BigDecimal("5.00")).build();
        when(orderItemCommandRepository.findById(1L)).thenReturn(Optional.of(item));
        when(orderCommandRepository.save(any(Order.class))).thenReturn(order);

        boolean result = orderCommandService.removeItem(1L);

        assertThat(result).isTrue();
        assertThat(order.getTotalPrice()).isEqualByComparingTo("20.00");
        verify(orderItemCommandRepository).deleteById(1L);
    }

    @Test
    void removeItem_itemNotFound_returnsFalse() {
        when(orderItemCommandRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = orderCommandService.removeItem(99L);

        assertThat(result).isFalse();
        verify(orderItemCommandRepository, never()).deleteById(anyLong());
    }
}
