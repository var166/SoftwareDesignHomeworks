package com.example.MVCOnlineMarketplace.Security;

import com.example.MVCOnlineMarketplace.Dto.UserDto;
import com.example.MVCOnlineMarketplace.Dto.UserRegistrationDto;
import com.example.MVCOnlineMarketplace.Model.User;
import com.example.MVCOnlineMarketplace.Service.UserMapper;
import com.example.MVCOnlineMarketplace.Service.UserService;
import com.example.MVCOnlineMarketplace.Service.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserSession {
    private static UserSession instance;
    private UserDto currentUser;



    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void login(UserDto user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public UserDto getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return currentUser != null && "Admin".equalsIgnoreCase(currentUser.getRole());
    }

    public boolean isStoreManager() {
        return currentUser != null && "StoreManager".equalsIgnoreCase(currentUser.getRole());
    }
}