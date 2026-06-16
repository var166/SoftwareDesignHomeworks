package andrei.shopservice.service;

import andrei.shopservice.model.Shop;

public interface IShopCommandService {
    Shop create(String name, String address, String phone, String email, String description, Long adminId);
    boolean updateName(long id, String name);
    boolean updateAddress(long id, String address);
    boolean updatePhone(long id, String phone);
    boolean updateEmail(long id, String email);
    boolean updateDescription(long id, String description);
    boolean delete(long id);
    boolean addProduct(long shopId, long productId, int initialStock);
    boolean removeProduct(long shopId, long productId);
    boolean updateStock(long shopId, long productId, int quantity);
    boolean incrementStock(long shopId, long productId, int amount);
    boolean decrementStock(long shopId, long productId, int amount);
}
