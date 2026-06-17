package com.example.MVCOnlineMarketplace.Service;

import com.example.MVCOnlineMarketplace.Dto.UserDto;
import com.example.MVCOnlineMarketplace.Model.User;
import com.example.MVCOnlineMarketplace.Model.UserRole;
import org.springframework.stereotype.Service;


public class UserMapper {
    public static UserDto mapToUserDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getUserRole().name())
                .password(user.getPassword())
                .build();
    }
    public static User mapFromUserDto(UserDto userDto){
        return User.builder().id(userDto.getId()).username(userDto.getUsername()).email(userDto.getEmail()).password(userDto.getPassword()).userRole(UserRole.valueOf(userDto.getRole())).build();
    }
}
