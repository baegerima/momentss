package com.example.moments.dto;


import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String profileImage;

    // Getters and Setters
}
