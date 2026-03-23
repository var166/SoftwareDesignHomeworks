package com.example.MVCOnlineMarketplace.Repositories;

import com.example.MVCOnlineMarketplace.Model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Long> {
}
