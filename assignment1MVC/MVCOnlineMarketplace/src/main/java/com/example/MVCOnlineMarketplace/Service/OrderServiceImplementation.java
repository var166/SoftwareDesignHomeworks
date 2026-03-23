package com.example.MVCOnlineMarketplace.Service;

import com.example.MVCOnlineMarketplace.Dto.OrderDto;
import com.example.MVCOnlineMarketplace.Model.Order;
import com.example.MVCOnlineMarketplace.Model.Product;
import com.example.MVCOnlineMarketplace.Repositories.OrderRepository;
import com.example.MVCOnlineMarketplace.Repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImplementation implements OrderService{

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    public OrderServiceImplementation(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }
    @Override
    public Optional<OrderDto> getOrderById(long id) {
        return orderRepository.findById(id).map(OrderMapper::mapToDto);
    }

    @Override
    public void save(OrderDto dto) {
        List<Long> productIds = dto.getOrderItems().stream().map(item -> item.getProductDto().getId()).toList();
        List<Product> products = productRepository.findAllById(productIds);
        Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, product -> product));

        Order order = OrderMapper.mapFromDto(dto, productMap);
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
        return orderRepository.findByUserId(userId).stream().map(OrderMapper::mapToDto).toList();
    }

    @Override
    public void updateOrderStatus(long orderId, boolean isPaid) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.isPaid(isPaid);
        orderRepository.save(order);
    }
}
