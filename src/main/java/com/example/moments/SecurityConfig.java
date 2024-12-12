package com.example.moments;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Используем BCrypt для шифрования паролей
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Отключаем CSRF для упрощения тестирования REST API
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll() // Доступ открыт для регистрации и входа
                        .requestMatchers("/api/users/**").permitAll() // Открытый доступ для эндпоинтов пользователей
                        .requestMatchers("/api/posts/**").permitAll() // Доступ к постам только для аутентифицированных пользователей
                        .requestMatchers("/api/favorites/**").permitAll()
                        .requestMatchers("/api/images/**").permitAll() // Доступ к изображениям открыт

                        .anyRequest().authenticated() // Остальные эндпоинты требуют аутентификации
                );
        return http.build();
    }
}
