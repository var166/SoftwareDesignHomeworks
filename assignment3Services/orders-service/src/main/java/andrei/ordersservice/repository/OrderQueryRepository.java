package andrei.ordersservice.repository;

import andrei.ordersservice.model.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface OrderQueryRepository extends Repository<Order, Long> {
    List<Order> findAll();
    List<Order> findAll(Sort sort);
    Order findByOrderId(long orderId);
    List<Order> findByUserId(Long userId);
    List<Order> findByUserId(Long userId, Sort sort);
    List<Order> findByIsPaid(boolean isPaid);
    List<Order> findByIsPaid(boolean isPaid, Sort sort);
    List<Order> findByUserIdAndIsPaid(Long userId, boolean isPaid);
    List<Order> findByUserIdAndIsPaid(Long userId, boolean isPaid, Sort sort);
}
