package andrei.usersservice.service;

import andrei.usersservice.model.User;
import andrei.usersservice.model.UserRole;

import java.util.List;
import java.util.Optional;

public interface IUserQueryRepository {
    Optional<User> authenticate(String username, String password);
    List<User> findAll();
    List<User> findAllSorted(String sortBy, String direction);
    List<User> findByRole(UserRole role);
    List<User> findByRoleSorted(UserRole role, String sortBy, String direction);
    User findByUsername(String username);
    User findByEmail(String email);
    User findById(long id);
}
