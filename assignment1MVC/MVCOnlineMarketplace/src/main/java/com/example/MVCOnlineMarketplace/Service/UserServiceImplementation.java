package com.example.MVCOnlineMarketplace.Service;

import com.example.MVCOnlineMarketplace.Dto.UserDto;
import com.example.MVCOnlineMarketplace.Model.User;
import com.example.MVCOnlineMarketplace.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImplementation implements UserService{

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


}
