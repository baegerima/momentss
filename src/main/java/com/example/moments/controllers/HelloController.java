package com.example.moments.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    // Обрабатывает запрос по пути "/hello"
    @GetMapping("/hello")
    public String hello() {
        return "✨ Moments are the magic of every day! ✨";
    }
}
