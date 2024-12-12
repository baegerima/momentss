package com.example.moments.image;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "images")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;

    @Lob
    @Column(name = "imagedata", length = 100000) // Увеличил длину для хранения больших данных
    private byte[] imageData;

    private String imagePath;
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }// Путь к изображению
    private String thumbnailPath; // Путь к миниатюре изображения


}
