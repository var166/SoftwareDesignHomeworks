package com.example.MVCOnlineMarketplace.Controller;

import com.example.MVCOnlineMarketplace.Dto.UserDto;
import com.example.MVCOnlineMarketplace.Model.User;
import com.example.MVCOnlineMarketplace.Service.UserMapper;
import com.example.MVCOnlineMarketplace.Service.UserService;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    public UserDto authenticate(String username, String password) {
        User user = userService.findByUsername(username).map(UserMapper::mapFromUserDto).get();
        if (user != null && user.getPassword().equals(password)) {
            return UserMapper.mapToUserDto(user);
        }
        return null;
    }
}