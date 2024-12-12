package com.example.moments.controllers;

import com.example.moments.entities.Post;
import com.example.moments.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/create")
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post savedPost = postService.savePost(post);
        return ResponseEntity.ok(savedPost);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post updatedPost) {
        return postService.updatePost(id, updatedPost)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
