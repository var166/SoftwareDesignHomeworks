package com.example.MVCOnlineMarketplace.Repositories;

import com.example.MVCOnlineMarketplace.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser_Id(long userId);
}
