package com.example.moments.services;

import com.example.moments.entities.User;
import com.example.moments.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Сохранение нового пользователя
    public User saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Получение пользователя по ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Получение всех пользователей
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Обновление пользователя
    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            existingUser.setProfileImage(updatedUser.getProfileImage());
            return userRepository.save(existingUser);
        });
    }

    // Удаление пользователя
    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
        return false;
    }

}
