package andrei.usersservice.repository;

import andrei.usersservice.model.User;
import andrei.usersservice.model.UserRole;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserCommandRepository extends Repository<User, Long> {

    User save(User user);

    void deleteById(long id);

    Optional<User> findById(long id);

    @Modifying
    @Query("UPDATE User u SET u.userRole = :role WHERE u.id = :id")
    void updateUserRole(@Param("id") long id, @Param("role") UserRole role);

    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    void updatePassword(@Param("id") long id, @Param("password") String password);
}