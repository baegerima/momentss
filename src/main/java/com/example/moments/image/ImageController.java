package com.example.moments.image;

import com.example.moments.entities.Post;
import com.example.moments.entities.User;
import com.example.moments.repositories.PostRepository;
import com.example.moments.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);
    private static final String IMAGE_DIR = "src/main/resources/static/";

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public ImageController(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @PostMapping("/upload/user/{userId}")
    public ResponseEntity<String> uploadUserImage(@PathVariable Long userId, @RequestParam("image") MultipartFile file) {
        if (file.isEmpty()) {
            logger.warn("Файл для пользователя с ID {} не предоставлен", userId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Файл не предоставлен");
        }

        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                logger.warn("Пользователь с ID {} не найден", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
            }

            // Создание директории, если она не существует
            Path directory = Paths.get(IMAGE_DIR + "users/");
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // Генерация имени файла и его путь
            String fileName = userId + "_" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
            Path filePath = directory.resolve(fileName);

            // Копирование файла
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Сохранение пути изображения в сущности пользователя и обновление базы данных
            user.setImagePath(filePath.toAbsolutePath().toString());
            userRepository.save(user);

            logger.info("Изображение пользователя успешно загружено для пользователя с ID {}: {}", userId, filePath);
            return ResponseEntity.ok("Изображение пользователя успешно загружено");
        } catch (IOException e) {
            logger.error("Ошибка при загрузке изображения для пользователя с ID {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при загрузке изображения");
        }
    }

    @PostMapping("/upload/post/{postId}")
    public ResponseEntity<String> uploadPostImage(@PathVariable Long postId, @RequestParam("image") MultipartFile file) {
        if (file.isEmpty()) {
            logger.warn("Файл для поста с ID {} не предоставлен", postId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Файл не предоставлен");
        }

        try {
            Post post = postRepository.findById(postId).orElse(null);
            if (post == null) {
                logger.warn("Пост с ID {} не найден", postId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пост не найден");
            }

            // Создание директории, если она не существует
            Path directory = Paths.get(IMAGE_DIR + "posts/");
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // Генерация имени файла и его путь
            String fileName = postId + "_" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
            Path filePath = directory.resolve(fileName);

            // Копирование файла
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Сохранение пути изображения в сущности поста и обновление базы данных
            post.setImagePath(filePath.toAbsolutePath().toString());
            postRepository.save(post);

            logger.info("Изображение поста успешно загружено для поста с ID {}: {}", postId, filePath);
            return ResponseEntity.ok("Изображение поста успешно загружено");
        } catch (IOException e) {
            logger.error("Ошибка при загрузке изображения для поста с ID {}", postId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при загрузке изображения");
        }
    }

    @GetMapping("/get/user/{userId}")
    public ResponseEntity<byte[]> getUserImage(@PathVariable Long userId) {
        logger.info("Получение изображения для пользователя с ID {}", userId);
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getImagePath() == null) {
            logger.warn("Пользователь с ID {} не найден или путь изображения пуст", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Path path = Paths.get(user.getImagePath());
        if (!Files.exists(path)) {
            logger.warn("Файл изображения не существует: {}", path);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            byte[] imageBytes = Files.readAllBytes(path);
            MediaType mediaType = determineMediaType(path);
            logger.info("Определенный тип медиа: {}", mediaType);
            return ResponseEntity.ok().contentType(mediaType).body(imageBytes);
        } catch (IOException e) {
            logger.error("Ошибка при чтении файла изображения: {}", path, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private MediaType determineMediaType(Path path) {
        try {
            String contentType = Files.probeContentType(path);
            if (contentType != null) {
                switch (contentType) {
                    case "image/jpeg": return MediaType.IMAGE_JPEG;
                    case "image/png": return MediaType.IMAGE_PNG;
                    case "image/gif": return MediaType.IMAGE_GIF;
                    default: return MediaType.APPLICATION_OCTET_STREAM;
                }
            }
        } catch (IOException e) {
            logger.error("Ошибка при определении типа медиа для файла: {}", path, e);
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }

    @GetMapping("/get/post/{postId}")
    public ResponseEntity<byte[]> getPostImage(@PathVariable Long postId) {
        logger.info("Получение изображения для поста с ID {}", postId);
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null || post.getImagePath() == null) {
            logger.warn("Пост с ID {} не найден или путь изображения пуст", postId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Path path = Paths.get(post.getImagePath());
        if (!Files.exists(path)) {
            logger.warn("Файл изображения не существует: {}", path);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            byte[] imageBytes = Files.readAllBytes(path);
            MediaType mediaType = determineMediaType(path);
            logger.info("Определенный тип медиа: {}", mediaType);
            return ResponseEntity.ok().contentType(mediaType).body(imageBytes);
        } catch (IOException e) {
            logger.error("Ошибка при чтении файла изображения: {}", path, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
