package andrei.usersservice.service;

import andrei.usersservice.model.User;
import andrei.usersservice.model.UserRole;
import andrei.usersservice.repository.UserCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandService implements IUserCommandService {

    private final UserCommandRepository userCommandRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User register(String username, String email, String password) {
        return userCommandRepository.save(
                User.builder()
                        .username(username)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .userRole(UserRole.USER)
                        .build()
        );
    }

    @Override
    @Transactional
    public boolean updateUserRole(long id, String userRole) {
        UserRole role;
        try {
            role = UserRole.valueOf(userRole.toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return userCommandRepository.findById(id)
                .map(user -> {
                    userCommandRepository.updateUserRole(id, role);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean updatePassword(long id, String password) {
        return userCommandRepository.findById(id)
                .map(user -> {
                    userCommandRepository.updatePassword(id, passwordEncoder.encode(password));
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean delete(long id) {
        return userCommandRepository.findById(id)
                .map(user -> {
                    userCommandRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
}
