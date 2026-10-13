package andrei.usersservice.repository;

import andrei.usersservice.model.User;
import andrei.usersservice.model.UserRole;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface UserQueryRepository extends Repository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findAll();
    List<User> findAll(Sort sort);
    List<User> findByUserRole(UserRole userRole);
    List<User> findByUserRole(UserRole userRole, Sort sort);
    User findById(long id);
}
