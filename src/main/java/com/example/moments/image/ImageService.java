package com.example.moments.image;

import com.example.moments.entities.Post;
import com.example.moments.entities.User;
import com.example.moments.repositories.PostRepository;
import com.example.moments.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);
    private static final String IMAGE_DIR = System.getProperty("user.dir") + "/src/main/resources/static/";

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public ImageService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public String uploadUserImage(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new IOException("User not found"));
        Path directory = Paths.get(IMAGE_DIR + "users/");
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        Path filePath = directory.resolve(userId + "_" + file.getOriginalFilename());
        file.transferTo(filePath.toFile());
        user.setImagePath(filePath.toString());
        userRepository.save(user);

        logger.info("User image uploaded successfully for user ID {}: {}", userId, filePath);
        return filePath.toString();
    }

    public String uploadPostImage(Long postId, MultipartFile file) throws IOException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IOException("Post not found"));
        Path directory = Paths.get(IMAGE_DIR + "posts/");
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        Path filePath = directory.resolve(postId + "_" + file.getOriginalFilename());
        file.transferTo(filePath.toFile());
        post.setImagePath(filePath.toString());
        postRepository.save(post);

        logger.info("Post image uploaded successfully for post ID {}: {}", postId, filePath);
        return filePath.toString();
    }

    public byte[] getUserImage(Long userId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new IOException("User not found"));
        if (user.getImagePath() == null) {
            logger.warn("Image path is null for user ID {}", userId);
            throw new IOException("Image path is null");
        }

        Path path = Paths.get(user.getImagePath());
        if (!Files.exists(path)) {
            logger.warn("Image file does not exist for user ID {}: {}", userId, path);
            throw new IOException("Image file does not exist");
        }

        return Files.readAllBytes(path);
    }
}
