package com.example.MVCOnlineMarketplace.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface Repository<T> extends JpaRepository<T, Long> {
}
