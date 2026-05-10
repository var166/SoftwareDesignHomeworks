package andrei.shopservice.repository;

import andrei.shopservice.model.Shop;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShopCommandRepository extends Repository<Shop, Long> {
    Shop save(Shop shop);
    void deleteById(long id);
    Optional<Shop> findById(long id);

    @Modifying
    @Query("UPDATE Shop s SET s.name = :name WHERE s.id = :id")
    void updateName(@Param("id") long id, @Param("name") String name);

    @Modifying
    @Query("UPDATE Shop s SET s.address = :address WHERE s.id = :id")
    void updateAddress(@Param("id") long id, @Param("address") String address);

    @Modifying
    @Query("UPDATE Shop s SET s.phone = :phone WHERE s.id = :id")
    void updatePhone(@Param("id") long id, @Param("phone") String phone);

    @Modifying
    @Query("UPDATE Shop s SET s.email = :email WHERE s.id = :id")
    void updateEmail(@Param("id") long id, @Param("email") String email);

    @Modifying
    @Query("UPDATE Shop s SET s.description = :description WHERE s.id = :id")
    void updateDescription(@Param("id") long id, @Param("description") String description);
}
