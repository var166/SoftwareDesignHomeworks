package com.example.MVCOnlineMarketplace.Repositories;

import com.example.MVCOnlineMarketplace.Model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
