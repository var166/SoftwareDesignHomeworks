package com.example.MVCOnlineMarketplace.Controller;

import com.example.MVCOnlineMarketplace.Dto.UserDto;
import com.example.MVCOnlineMarketplace.Dto.UserRegistrationDto;
import com.example.MVCOnlineMarketplace.Service.UserService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public UserDto authenticate(String username, String password) {
        return userService.authenticate(username, password);
    }

    public UserDto register(UserRegistrationDto dto) {
        return userService.register(dto);
    }

    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    public List<UserDto> getFiltered(String column, String value, String sortBy, boolean ascending) {
        return userService.getFiltered(column, value, sortBy, ascending);
    }

    public void saveUser(UserDto dto) {
        userService.save(dto);
    }

    public void deleteUserById(long id) {
        userService.deleteUserById(id);
    }

    public void updateUserRole(long userId, String role) {
        userService.updateUserRole(userId, role);
    }

    public UserDto findById(long id) {
        return userService.findById(id).orElse(null);
    }
}
