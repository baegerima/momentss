package com.example.moments.services;

import com.example.moments.entities.Post;
import com.example.moments.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // Сохранение нового поста
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    // Получение поста по ID
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    // Получение всех постов
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // Обновление поста
    public Optional<Post> updatePost(Long id, Post updatedPost) {
        return postRepository.findById(id).map(existingPost -> {
            existingPost.setTitle(updatedPost.getTitle());
            existingPost.setContent(updatedPost.getContent());
            return postRepository.save(existingPost);
        });
    }

    // Удаление поста
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("Post with id " + id + " not found");
        }
        postRepository.deleteById(id);
    }
}
