package com.example.moments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.moments")
public class MomentsApplication {
    public static void main(String[] args) {
        SpringApplication.run(MomentsApplication.class, args);
    }
}
