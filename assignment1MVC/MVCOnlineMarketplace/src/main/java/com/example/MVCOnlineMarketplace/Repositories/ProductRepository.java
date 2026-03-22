package com.example.MVCOnlineMarketplace.Repositories;

import com.example.MVCOnlineMarketplace.Model.Product;

import java.util.List;

public interface ProductRepository extends Repository<Product>{
    public List<Product> findByUser();
}
