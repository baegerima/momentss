package com.example.moments.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequest {
    private String token;
    private String newPassword;
}
