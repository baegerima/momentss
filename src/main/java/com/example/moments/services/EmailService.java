package com.example.moments.services;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendEmail(String to, String subject, String body) {
        // Здесь используйте библиотеку для отправки email, например JavaMailSender
        System.out.printf("Email sent to %s: %s%n", to, body);
    }
}

