package andrei.usersservice.service;

import andrei.usersservice.model.User;
import andrei.usersservice.model.UserRole;
import andrei.usersservice.repository.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserQueryService implements IUserQueryRepository {

    private final UserQueryRepository userQueryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> authenticate(String username, String password) {
        User user = userQueryRepository.findByUsername(username);
        if (user == null) return Optional.empty();
        if (!passwordEncoder.matches(password, user.getPassword())) return Optional.empty();
        return Optional.of(user);
    }

    @Override
    public List<User> findAll() {
        return userQueryRepository.findAll();
    }

    @Override
    public List<User> findAllSorted(String sortBy, String direction) {
        return userQueryRepository.findAll(Sort.by(Sort.Direction.fromString(direction), sortBy));
    }

    @Override
    public List<User> findByRole(UserRole role) {
        return userQueryRepository.findByUserRole(role);
    }

    @Override
    public List<User> findByRoleSorted(UserRole role, String sortBy, String direction) {
        return userQueryRepository.findByUserRole(role, Sort.by(Sort.Direction.fromString(direction), sortBy));
    }

    @Override
    public User findByUsername(String username) {
        return userQueryRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userQueryRepository.findByEmail(email);
    }

    @Override
    public User findById(long id) {
        return userQueryRepository.findById(id);
    }
}
