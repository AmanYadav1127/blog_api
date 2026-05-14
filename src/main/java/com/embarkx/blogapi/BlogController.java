package com.embarkx.blogapi;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class BlogController {

    private static List<Post> posts = new ArrayList<>();

    @Value("${blog.validation.title.max-length}")
    private int titleMaxLength;

    @Value("${blog.validation.content.max-length}")
    private int contentMaxLength;

    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody Post post) {
        // Validate inputs
        if (post.getTitle() == null || post.getTitle().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Title cannot be empty");
        }
        if (post.getContent() == null || post.getContent().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Content cannot be empty");
        }
        if (post.getTitle().length() > titleMaxLength) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Title is too long (max " + titleMaxLength + " characters)");
        }
        if (post.getContent().length() > contentMaxLength) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Content is too long (max " + contentMaxLength + " characters)");
        }

        Post newPost = new Post(posts.size(), post.getTitle(), post.getContent());
        posts.add(newPost);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post created");
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return posts;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable int id) {
        if (id < 0 || id >= posts.size()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(posts.get(id));
    }


    @PostMapping("/validate")
    public ResponseEntity<String> validateContent(@RequestParam String content) {
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Content cannot be empty");
        }
        if (content.length() > contentMaxLength) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Content is too long (max " + contentMaxLength + " characters)");
        }
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable int id) {
        if (id < 0 || id >= posts.size()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Cannot delete (invalid id)");
        }
        posts.remove(id);
        return ResponseEntity.status(HttpStatus.OK).body("Post deleted successfully");
    }

    @GetMapping("/total")
    public ResponseEntity<String> getTotalWordCount() {
        List<String> wordCounts = List.of("100", "200", "300");
        int total = 0;
        for (String count : wordCounts) {
            try {
                total += Integer.parseInt(count);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Invalid number format");
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Total words: " + total);
    }
}