package andrei.ordersservice.service;

import andrei.ordersservice.model.Order;
import andrei.ordersservice.model.OrderItem;
import andrei.ordersservice.repository.OrderItemQueryRepository;
import andrei.ordersservice.repository.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderQueryService implements IOrderQueryService {

    private final OrderQueryRepository orderQueryRepository;
    private final OrderItemQueryRepository orderItemQueryRepository;

    @Override
    public List<Order> findAll() {
        return orderQueryRepository.findAll();
    }

    @Override
    public List<Order> findAllSorted(String sortBy, String direction) {
        return orderQueryRepository.findAll(Sort.by(Sort.Direction.fromString(direction), sortBy));
    }

    @Override
    public Order findById(long orderId) {
        return orderQueryRepository.findByOrderId(orderId);
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderQueryRepository.findByUserId(userId);
    }

    @Override
    public List<Order> findByUserIdSorted(Long userId, String sortBy, String direction) {
        return orderQueryRepository.findByUserId(userId, Sort.by(Sort.Direction.fromString(direction), sortBy));
    }

    @Override
    public List<Order> findByIsPaid(boolean isPaid) {
        return orderQueryRepository.findByIsPaid(isPaid);
    }

    @Override
    public List<Order> findByIsPaidSorted(boolean isPaid, String sortBy, String direction) {
        return orderQueryRepository.findByIsPaid(isPaid, Sort.by(Sort.Direction.fromString(direction), sortBy));
    }

    @Override
    public List<Order> findByUserIdAndIsPaid(Long userId, boolean isPaid) {
        return orderQueryRepository.findByUserIdAndIsPaid(userId, isPaid);
    }

    @Override
    public List<Order> findByUserIdAndIsPaidSorted(Long userId, boolean isPaid, String sortBy, String direction) {
        return orderQueryRepository.findByUserIdAndIsPaid(userId, isPaid, Sort.by(Sort.Direction.fromString(direction), sortBy));
    }

    @Override
    public List<OrderItem> findItemsByOrderId(long orderId) {
        return orderItemQueryRepository.findByOrderOrderId(orderId);
    }
}
