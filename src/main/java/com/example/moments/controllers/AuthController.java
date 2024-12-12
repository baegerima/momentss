package com.example.moments.controllers;

import com.example.moments.dto.LoginRequest;
import com.example.moments.dto.RegistrationRequest;
import com.example.moments.dto.UpdatePasswordRequest;
import com.example.moments.dto.EmailRequest;
import com.example.moments.entities.User;
import com.example.moments.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegistrationRequest request) {
        try {
            User registeredUser = authService.registerUser(request);
            return ResponseEntity.ok("User registered successfully: " + registeredUser.getUsername());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.login(loginRequest);
            return ResponseEntity.ok("Login successful. Token: " + token);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody EmailRequest emailRequest) {
        try {
            authService.sendResetPasswordEmail(emailRequest.getEmail());
            return ResponseEntity.ok("Password reset email sent successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Password reset failed: " + e.getMessage());
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        try {
            authService.updatePassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok("Password updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Password update failed: " + e.getMessage());
        }
    }
}
