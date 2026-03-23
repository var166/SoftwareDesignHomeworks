package com.example.MVCOnlineMarketplace.Dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UserRegistrationDto {
    private String email;
    private String password;
}
