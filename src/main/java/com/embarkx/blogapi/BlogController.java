package com.embarkx.blogapi;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class BlogController {

    private static List<String> posts = new ArrayList<>();

    @PostMapping
    public ResponseEntity<String> createPost(@RequestParam String title, @RequestParam String content) {
        // Validate inputs
        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Title cannot be empty");
        }
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Content cannot be empty");
        }
        if (title.length() > 200) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Title is too long (max 200 characters)");
        }
        if (content.length() > 5000) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Content is too long (max 5000 characters)");
        }
        
        String post = title + ":" + content;
        posts.add(post);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post created");
    }

    @GetMapping
    public List<String> getAllPosts() {
        return posts;
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getPost(@PathVariable int id) {
        if (id < 0 || id >= posts.size()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Post not found (invalid id)");
        }
        return ResponseEntity.status(HttpStatus.OK).body(posts.get(id));
    }


    @PostMapping("/validate")
    public ResponseEntity<String> validateContent(@RequestParam String content) {
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Content cannot be empty");
        }
        if (content.length() > 5000) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Content is too long (max 5000 characters)");
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