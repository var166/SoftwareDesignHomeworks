package com.example.MVCOnlineMarketplace.Service;

import com.example.MVCOnlineMarketplace.Dto.UserDto;
import com.example.MVCOnlineMarketplace.Dto.UserRegistrationDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserDto> findByUsername(String username);
    Optional<UserDto> findById(long id);
    Optional<UserDto> findByEmail(String email);
    void save(UserDto user);
    void delete(UserDto user);
    boolean existsByUsername(String username);
    UserDto authenticate(String username, String password);
    UserDto register(UserRegistrationDto registrationDto);
    List<UserDto> getAllUsers();
    List<UserDto> getFiltered(String column, String value, String sortBy, boolean ascending);
    void updateUserRole(long userId, String role);
    void deleteUserById(long id);
}
