package andrei.shopservice.service;

import andrei.shopservice.model.Shop;
import andrei.shopservice.repository.ShopCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShopCommandService implements IShopCommandService {

    private final ShopCommandRepository shopCommandRepository;

    @Override
    @Transactional
    public Shop create(String name, String address, String phone, String email, String description, Long adminId) {
        return shopCommandRepository.save(
                Shop.builder()
                        .name(name)
                        .address(address)
                        .phone(phone)
                        .email(email)
                        .description(description)
                        .adminId(adminId)
                        .build()
        );
    }

    @Override
    @Transactional
    public boolean updateName(long id, String name) {
        return shopCommandRepository.findById(id)
                .map(shop -> {
                    shopCommandRepository.updateName(id, name);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean updateAddress(long id, String address) {
        return shopCommandRepository.findById(id)
                .map(shop -> {
                    shopCommandRepository.updateAddress(id, address);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean updatePhone(long id, String phone) {
        return shopCommandRepository.findById(id)
                .map(shop -> {
                    shopCommandRepository.updatePhone(id, phone);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean updateEmail(long id, String email) {
        return shopCommandRepository.findById(id)
                .map(shop -> {
                    shopCommandRepository.updateEmail(id, email);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean updateDescription(long id, String description) {
        return shopCommandRepository.findById(id)
                .map(shop -> {
                    shopCommandRepository.updateDescription(id, description);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean delete(long id) {
        return shopCommandRepository.findById(id)
                .map(shop -> {
                    shopCommandRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean addProduct(long shopId, long productId, int initialStock) {
        return shopCommandRepository.findById(shopId)
                .map(shop -> {
                    shop.getProductStock().putIfAbsent(productId, initialStock);
                    shopCommandRepository.save(shop);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean removeProduct(long shopId, long productId) {
        return shopCommandRepository.findById(shopId)
                .map(shop -> {
                    shop.getProductStock().remove(productId);
                    shopCommandRepository.save(shop);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean updateStock(long shopId, long productId, int quantity) {
        return shopCommandRepository.findById(shopId)
                .filter(shop -> shop.getProductStock().containsKey(productId))
                .map(shop -> {
                    shop.getProductStock().put(productId, quantity);
                    shopCommandRepository.save(shop);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean incrementStock(long shopId, long productId, int amount) {
        return shopCommandRepository.findById(shopId)
                .filter(shop -> shop.getProductStock().containsKey(productId))
                .map(shop -> {
                    shop.getProductStock().merge(productId, amount, Integer::sum);
                    shopCommandRepository.save(shop);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean decrementStock(long shopId, long productId, int amount) {
        return shopCommandRepository.findById(shopId)
                .filter(shop -> shop.getProductStock().containsKey(productId))
                .map(shop -> {
                    int current = shop.getProductStock().get(productId);
                    if (current < amount) return false;
                    shop.getProductStock().put(productId, current - amount);
                    shopCommandRepository.save(shop);
                    return true;
                })
                .orElse(false);
    }
}
