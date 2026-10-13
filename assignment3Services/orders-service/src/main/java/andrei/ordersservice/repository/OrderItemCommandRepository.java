package andrei.ordersservice.repository;

import andrei.ordersservice.model.OrderItem;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface OrderItemCommandRepository extends Repository<OrderItem, Long> {
    OrderItem save(OrderItem orderItem);
    void deleteById(long id);
    Optional<OrderItem> findById(long id);
}
