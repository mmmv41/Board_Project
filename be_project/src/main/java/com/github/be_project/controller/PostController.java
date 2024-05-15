package com.github.be_project.controller;

import com.github.be_project.entity.Post;
import com.github.be_project.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<Post>> getPosts() {
        List<Post> posts = postService.getPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable("id") int id) {
        Post post = postService.getPost(id);
        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        post.setCreated_At(LocalDateTime.now());
        Post savedPost = postService.save(post);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable("id") int id, @RequestBody Post updatedPost) {
        updatedPost.setId(id);
        updatedPost.setUpdated_At(LocalDateTime.now());
        Post post = postService.editPost(id, updatedPost);
        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") int id) {
        postService.deletePost(id);
        return ResponseEntity.ok("Deleted");
    }

    // 좋아요 처리
    @PostMapping("/{id}/like")
    public ResponseEntity<Post> likePost(@PathVariable("id") int id) {
        Post post = postService.getPost(id);
        if (post != null) {
            post.setLiked(post.getLiked() + 1); // 좋아요 증가
            Post updatedPost = postService.save(post);
            return ResponseEntity.ok(updatedPost);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
