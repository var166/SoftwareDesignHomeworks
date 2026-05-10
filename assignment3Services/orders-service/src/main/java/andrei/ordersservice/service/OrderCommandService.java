package andrei.ordersservice.service;

import andrei.ordersservice.dto.OrderItemDto;
import andrei.ordersservice.model.Order;
import andrei.ordersservice.model.OrderItem;
import andrei.ordersservice.repository.OrderCommandRepository;
import andrei.ordersservice.repository.OrderItemCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderCommandService implements IOrderCommandService {

    private final OrderCommandRepository orderCommandRepository;
    private final OrderItemCommandRepository orderItemCommandRepository;

    @Override
    @Transactional
    public Order createOrder(Long userId, List<OrderItemDto.OrderItemRequestDto> items) {
        Order order = Order.builder()
                .userId(userId)
                .isPaid(false)
                .totalPrice(BigDecimal.ZERO)
                .orderItems(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemDto.OrderItemRequestDto item : items) {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productId(item.productId())
                    .quantity(item.quantity())
                    .pricePerUnit(item.pricePerUnit())
                    .build();
            order.getOrderItems().add(orderItem);
            total = total.add(item.pricePerUnit().multiply(BigDecimal.valueOf(item.quantity())));
        }
        order.setTotalPrice(total);
        return orderCommandRepository.save(order);
    }

    @Override
    @Transactional
    public boolean markAsPaid(long orderId) {
        return orderCommandRepository.findById(orderId)
                .map(order -> {
                    orderCommandRepository.updateIsPaid(orderId, true);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean deleteOrder(long orderId) {
        return orderCommandRepository.findById(orderId)
                .map(order -> {
                    orderCommandRepository.deleteById(orderId);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public OrderItem addItem(long orderId, Long productId, int quantity, BigDecimal pricePerUnit) {
        return orderCommandRepository.findById(orderId)
                .map(order -> {
                    OrderItem item = OrderItem.builder()
                            .order(order)
                            .productId(productId)
                            .quantity(quantity)
                            .pricePerUnit(pricePerUnit)
                            .build();
                    OrderItem saved = orderItemCommandRepository.save(item);
                    BigDecimal addition = pricePerUnit.multiply(BigDecimal.valueOf(quantity));
                    order.setTotalPrice(order.getTotalPrice().add(addition));
                    orderCommandRepository.save(order);
                    return saved;
                })
                .orElse(null);
    }

    @Override
    @Transactional
    public boolean removeItem(long itemId) {
        return orderItemCommandRepository.findById(itemId)
                .map(item -> {
                    Order order = item.getOrder();
                    BigDecimal deduction = item.getPricePerUnit().multiply(BigDecimal.valueOf(item.getQuantity()));
                    order.setTotalPrice(order.getTotalPrice().subtract(deduction));
                    orderCommandRepository.save(order);
                    orderItemCommandRepository.deleteById(itemId);
                    return true;
                })
                .orElse(false);
    }
}
