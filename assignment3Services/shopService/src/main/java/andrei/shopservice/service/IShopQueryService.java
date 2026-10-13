package andrei.shopservice.service;

import andrei.shopservice.model.Shop;

import java.util.List;

public interface IShopQueryService {
    List<Shop> findAll();
    List<Shop> findAllSorted(String sortBy, String direction);
    Shop findById(long id);
    Shop findByName(String name);
    Shop findByEmail(String email);
    List<Shop> findByAdminId(Long adminId);
    List<Shop> findByAdminIdSorted(Long adminId, String sortBy, String direction);
}
