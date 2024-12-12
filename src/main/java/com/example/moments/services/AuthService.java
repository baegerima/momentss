package com.example.moments.services;
import com.example.moments.dto.RegistrationRequest;
import com.example.moments.dto.LoginRequest;
import com.example.moments.entities.User;
import com.example.moments.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtService jwtService;

    public User registerUser(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            logger.warn("Attempt to register with existing email: {}", request.getEmail());
            throw new RuntimeException("User with this email already exists");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        logger.info("User registered successfully: {}", request.getEmail());
        return userRepository.save(newUser);
    }

    public String login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            logger.warn("Invalid login attempt for email: {}", loginRequest.getEmail());
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);
        logger.info("User logged in successfully: {}", loginRequest.getEmail());
        return token;
    }

    public String sendResetPasswordEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        String resetToken = jwtService.generateToken(user);
        String resetLink = "http://yourfrontend.com/reset-password?token=" + resetToken;

        emailService.sendEmail(email, "Reset Password",
                "Hello, " + user.getUsername() + "! Click the link to reset your password: " + resetLink);

        logger.info("Password reset email sent to: {}", email);
        return resetToken;
    }

    public void updatePassword(String token, String newPassword) {
        String email = jwtService.extractEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (newPassword.length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters long");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        logger.info("Password updated successfully for email: {}", email);
    }
}
