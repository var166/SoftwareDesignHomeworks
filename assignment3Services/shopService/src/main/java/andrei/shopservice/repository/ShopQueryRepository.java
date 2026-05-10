package andrei.shopservice.repository;

import andrei.shopservice.model.Shop;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ShopQueryRepository extends Repository<Shop, Long> {
    List<Shop> findAll();
    List<Shop> findAll(Sort sort);
    Shop findById(long id);
    Shop findByName(String name);
    Shop findByEmail(String email);
    List<Shop> findByAdminId(Long adminId);
    List<Shop> findByAdminId(Long adminId, Sort sort);
}
