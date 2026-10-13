package com.example.MVCOnlineMarketplace.Service;

import com.example.MVCOnlineMarketplace.Dto.UserDto;
import com.example.MVCOnlineMarketplace.Dto.UserRegistrationDto;
import com.example.MVCOnlineMarketplace.Model.User;
import com.example.MVCOnlineMarketplace.Model.UserRole;
import com.example.MVCOnlineMarketplace.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserDto> findByUsername(String username) {
        return userRepository.findByUsername(username).map(UserMapper::mapToUserDto);
    }

    @Override
    public Optional<UserDto> findById(long id) {
        return userRepository.findById(id).map(UserMapper::mapToUserDto);
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserMapper::mapToUserDto);
    }

    @Override
    public void save(UserDto user) {
        userRepository.save(UserMapper.mapFromUserDto(user));
    }

    @Override
    public void delete(UserDto user) {
        userRepository.delete(UserMapper.mapFromUserDto(user));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDto authenticate(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(password)) {
            return UserMapper.mapToUserDto(userOptional.get());
        }
        return null;
    }

    @Override
    public UserDto register(UserRegistrationDto registrationDto) {
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        User user = User.builder()
                .username(registrationDto.getUsername())
                .email(registrationDto.getEmail())
                .password(registrationDto.getPassword())
                .userRole(UserRole.User)
                .build();
        return UserMapper.mapToUserDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::mapToUserDto).toList();
    }

    @Override
    public List<UserDto> getFiltered(String column, String value, String sortBy, boolean ascending) {
        List<UserDto> list = new ArrayList<>(getAllUsers());
        if (column != null && value != null && !value.isBlank()) {
            String lower = value.toLowerCase();
            list = list.stream().filter(u -> {
                if ("username".equals(column)) return u.getUsername() != null && u.getUsername().toLowerCase().contains(lower);
                if ("email".equals(column)) return u.getEmail() != null && u.getEmail().toLowerCase().contains(lower);
                if ("role".equals(column)) return u.getRole() != null && u.getRole().toLowerCase().contains(lower);
                return true;
            }).collect(Collectors.toList());
        }
        if (sortBy != null && !sortBy.isBlank()) {
            list = list.stream().sorted((a, b) -> {
                int cmp;
                switch (sortBy) {
                    case "username": cmp = nullSafeCompare(a.getUsername(), b.getUsername()); break;
                    case "email": cmp = nullSafeCompare(a.getEmail(), b.getEmail()); break;
                    case "role": cmp = nullSafeCompare(a.getRole(), b.getRole()); break;
                    default: cmp = Long.compare(a.getId(), b.getId());
                }
                return ascending ? cmp : -cmp;
            }).collect(Collectors.toList());
        }
        return list;
    }

    private int nullSafeCompare(String a, String b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareToIgnoreCase(b);
    }

    @Override
    public void updateUserRole(long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUserRole(UserRole.valueOf(role));
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(long id) {
        userRepository.deleteById(id);
    }
}
