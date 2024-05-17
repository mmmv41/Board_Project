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

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<List<Post>> getPosts() {
        // 게시글 전체를 조회하여 반환
        List<Post> posts = postService.getPosts();
        return ResponseEntity.ok(posts);
    }

    // 작성자의 게시글 조회
    @GetMapping("/search")
    public ResponseEntity<List<Post>> getPostsByAuthor(@RequestParam("author") String authorEmail) {
        // 주어진 작성자의 이메일로 게시글을 조회하여 반환
        List<Post> posts = postService.getPostsByAuthor(authorEmail);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // id로 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable("id") int id) {
        // 주어진 id로 게시글을 조회
        Post post = postService.getPost(id);
        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 게시글 작성
    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody Post post) {
        // 주어진 게시글을 작성하고 성공 메시지를 반환
        post.setCreated_At(LocalDateTime.now());
        try {
            postService.save(post);
            return new ResponseEntity<>("게시글이 성공적으로 작성되었습니다", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            if ("로그인이 필요합니다".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("에러입니다");
        }
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePost(@PathVariable("id") int id, @RequestBody Post updatedPost) {
        // 주어진 id로 게시글을 찾아 수정하고 성공 메시지를 반환
        Post existingPost = postService.getPost(id);
        if (existingPost == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            // 게시물의 ID를 설정
            updatedPost.setId(id);
            // 수정된 시간을 설정
            updatedPost.setUpdated_At(LocalDateTime.now());
            // 수정된 게시물을 저장하고 반환
            postService.editPost(id, updatedPost);
            return ResponseEntity.ok("게시글이 성공적으로 수정되었습니다");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다");
        }
    }


    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") int id, @RequestParam("author") String author) {
        // 주어진 id와 작성자로 게시글을 삭제하고 성공 메시지를 반환
        try {
            postService.deletePost(id, author);
            return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다");
        }
    }

    // 좋아요 처리
    @PostMapping("/{id}/like")
    public ResponseEntity<String> likePost(@PathVariable("id") int id) {
        // 주어진 id로 게시글을 찾아 좋아요를 누르고 성공 메시지를 반환
        Post post = postService.getPost(id);
        if (post != null) {
            post.setLiked(post.getLiked() + 1); // 좋아요 증가
            postService.save(post);
            return ResponseEntity.ok("좋아요를 누르셨습니다");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}