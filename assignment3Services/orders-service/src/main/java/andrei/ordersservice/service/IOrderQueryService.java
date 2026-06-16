package andrei.ordersservice.service;

import andrei.ordersservice.model.Order;
import andrei.ordersservice.model.OrderItem;

import java.util.List;

public interface IOrderQueryService {
    List<Order> findAll();
    List<Order> findAllSorted(String sortBy, String direction);
    Order findById(long orderId);
    List<Order> findByUserId(Long userId);
    List<Order> findByUserIdSorted(Long userId, String sortBy, String direction);
    List<Order> findByIsPaid(boolean isPaid);
    List<Order> findByIsPaidSorted(boolean isPaid, String sortBy, String direction);
    List<Order> findByUserIdAndIsPaid(Long userId, boolean isPaid);
    List<Order> findByUserIdAndIsPaidSorted(Long userId, boolean isPaid, String sortBy, String direction);
    List<OrderItem> findItemsByOrderId(long orderId);
}
