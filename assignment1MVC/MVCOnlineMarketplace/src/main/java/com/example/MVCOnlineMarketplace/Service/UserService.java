package com.example.MVCOnlineMarketplace.Service;

import com.example.MVCOnlineMarketplace.Dto.UserDto;
import com.example.MVCOnlineMarketplace.Model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    public Optional<UserDto> findByUsername(String username);
    public Optional<UserDto> findById(long id);
    public Optional<UserDto> findByEmail(String email);
    public void save(UserDto user);
    public void delete(UserDto user);
    public boolean existsByUsername(String username);
    public UserDto authenticate(String username, String password);
}
