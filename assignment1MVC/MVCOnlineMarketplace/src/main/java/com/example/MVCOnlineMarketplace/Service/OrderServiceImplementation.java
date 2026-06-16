package com.example.MVCOnlineMarketplace.Service;

import com.example.MVCOnlineMarketplace.Dto.OrderDto;
import com.example.MVCOnlineMarketplace.Model.Order;
import com.example.MVCOnlineMarketplace.Model.Product;
import com.example.MVCOnlineMarketplace.Model.User;
import com.example.MVCOnlineMarketplace.Repositories.OrderRepository;
import com.example.MVCOnlineMarketplace.Repositories.ProductRepository;
import com.example.MVCOnlineMarketplace.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderServiceImplementation(OrderRepository orderRepository,
                                      ProductRepository productRepository,
                                      UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<OrderDto> getOrderById(long id) {
        return orderRepository.findById(id).map(OrderMapper::mapToDto);
    }

    @Override
    public void save(OrderDto dto) {
        User user = userRepository.findById(dto.getUserDto().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Long> productIds = dto.getOrderItems().stream()
                .map(item -> item.getProductDto().getId())
                .toList();
        List<Product> products = productRepository.findAllById(productIds);
        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        Order order = OrderMapper.mapFromDto(dto, productMap, user);
        orderRepository.save(order);
    }

    @Override
    public void deleteById(long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public boolean existsById(long id) {
        return orderRepository.existsById(id);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream().map(OrderMapper::mapToDto).toList();
    }

    @Override
    public List<OrderDto> getOrdersByUserId(long userId) {
        return orderRepository.findByUser_Id(userId).stream().map(OrderMapper::mapToDto).toList();
    }

    @Override
    public List<OrderDto> getFilteredByUserId(long userId, String column, String value, String sortBy, boolean ascending) {
        List<OrderDto> list = new ArrayList<>(getOrdersByUserId(userId));
        if (column != null && value != null && !value.isBlank()) {
            String lower = value.toLowerCase();
            list = list.stream().filter(o -> {
                if ("totalPrice".equals(column)) return o.getTotalPrice() != null && o.getTotalPrice().toPlainString().contains(value);
                if ("isPaid".equals(column)) {
                    boolean paid = "paid".equals(lower);
                    return o.isPaid() == paid;
                }
                return true;
            }).collect(Collectors.toList());
        }
        if (sortBy != null && !sortBy.isBlank()) {
            list = list.stream().sorted((a, b) -> {
                int cmp;
                switch (sortBy) {
                    case "totalPrice": cmp = a.getTotalPrice() != null && b.getTotalPrice() != null ? a.getTotalPrice().compareTo(b.getTotalPrice()) : 0; break;
                    case "isPaid": cmp = Boolean.compare(a.isPaid(), b.isPaid()); break;
                    default: cmp = Long.compare(a.getId(), b.getId());
                }
                return ascending ? cmp : -cmp;
            }).collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public void updateOrderStatus(long orderId, boolean isPaid) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setIsPaid(isPaid);
        orderRepository.save(order);
    }
}
