package andrei.shopservice.service;

import andrei.shopservice.model.Shop;
import andrei.shopservice.repository.ShopQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopQueryService implements IShopQueryService {

    private final ShopQueryRepository shopQueryRepository;

    @Override
    public List<Shop> findAll() {
        return shopQueryRepository.findAll();
    }

    @Override
    public List<Shop> findAllSorted(String sortBy, String direction) {
        return shopQueryRepository.findAll(Sort.by(Sort.Direction.fromString(direction), sortBy));
    }

    @Override
    public Shop findById(long id) {
        return shopQueryRepository.findById(id);
    }

    @Override
    public Shop findByName(String name) {
        return shopQueryRepository.findByName(name);
    }

    @Override
    public Shop findByEmail(String email) {
        return shopQueryRepository.findByEmail(email);
    }

    @Override
    public List<Shop> findByAdminId(Long adminId) {
        return shopQueryRepository.findByAdminId(adminId);
    }

    @Override
    public List<Shop> findByAdminIdSorted(Long adminId, String sortBy, String direction) {
        return shopQueryRepository.findByAdminId(adminId, Sort.by(Sort.Direction.fromString(direction), sortBy));
    }
}
