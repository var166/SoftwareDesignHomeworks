package andrei.ordersservice.repository;

import andrei.ordersservice.model.OrderItem;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface OrderItemQueryRepository extends Repository<OrderItem, Long> {
    List<OrderItem> findByOrderOrderId(long orderId);
    OrderItem findById(long id);
}
