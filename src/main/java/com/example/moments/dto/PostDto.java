package com.example.moments.dto;

import lombok.Data;

@Data
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String status;
    private String createdAt;
    private String updatedAt;

    // Getters and Setters
}
