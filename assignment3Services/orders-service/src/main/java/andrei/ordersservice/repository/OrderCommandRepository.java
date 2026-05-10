package andrei.ordersservice.repository;

import andrei.ordersservice.model.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderCommandRepository extends Repository<Order, Long> {
    Order save(Order order);
    void deleteById(long id);
    Optional<Order> findById(long id);

    @Modifying
    @Query("UPDATE Order o SET o.isPaid = :isPaid WHERE o.orderId = :id")
    void updateIsPaid(@Param("id") long id, @Param("isPaid") boolean isPaid);
}
