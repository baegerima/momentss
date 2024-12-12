package com.example.moments.controllers;

import com.example.moments.entities.Favorite;
import com.example.moments.entities.User;
import com.example.moments.entities.Post;
import com.example.moments.services.FavoriteService;
import com.example.moments.services.UserService;
import com.example.moments.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserService userService; // Сервис для работы с User

    @Autowired
    private PostService postService; // Сервис для работы с Post

    @PostMapping("/create")
    public ResponseEntity<Favorite> createFavorite(@RequestBody Favorite favorite) {
        if (favorite.getUser() == null || favorite.getPost() == null) {
            return ResponseEntity.badRequest().build(); // Возвращаем ошибку, если объекты отсутствуют
        }

        // Получаем пользователя и пост из базы данных
        Optional<User> userOpt = userService.getUserById(favorite.getUser().getId());
        Optional<Post> postOpt = postService.getPostById(favorite.getPost().getId());

        if (userOpt.isEmpty() || postOpt.isEmpty()) {
            return ResponseEntity.badRequest().build(); // Возвращаем ошибку, если пользователь или пост не найден
        }

        // Устанавливаем найденные объекты в объект favorite
        favorite.setUser(userOpt.get());
        favorite.setPost(postOpt.get());

        Favorite createdFavorite = favoriteService.saveFavorite(favorite);
        return ResponseEntity.ok(createdFavorite);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Favorite> getFavoriteById(@PathVariable Long id) {
        return favoriteService.getFavoriteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/get-all")
    public List<Favorite> getAllFavorites() {
        return favoriteService.getAllFavorites();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }
}
