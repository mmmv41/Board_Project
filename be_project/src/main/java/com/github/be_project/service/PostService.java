package com.github.be_project.service;

import com.github.be_project.entity.Post;
import com.github.be_project.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 모든 게시글 가져옴
    @Transactional(readOnly = true)
    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    // 특정 ID에 해당하는 게시글 가져옴
    @Transactional(readOnly = true)
    public Post getPost(int id) {
        return postRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByAuthor(String author) {
        return postRepository.findByAuthor(author);
    }

    // 게시글 작성
    @Transactional
    public Post save(Post post) {
        if (post.getAuthor() == null || post.getAuthor().isEmpty()) {
            throw new IllegalArgumentException("로그인이 필요합니다");
        }

        //author가 유효한지 확인
        List<Post> authorPosts = postRepository.findByAuthor(post.getAuthor());
        if (authorPosts.isEmpty()) {
            throw new IllegalArgumentException("로그인이 필요합니다");
        }

        return postRepository.save(post);
    }

    // 게시글 수정
    @Transactional
    public Post editPost(int id, Post updatedPost) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            if (!post.getAuthor().equals(updatedPost.getAuthor())) {
                throw new IllegalArgumentException("수정 권한이 없습니다");
            }
            post.setTitle(updatedPost.getTitle());
            post.setContent(updatedPost.getContent());
            post.setLiked(updatedPost.getLiked());
            post.setUpdated_At(updatedPost.getUpdated_At());
            return postRepository.save(post);
        }
        return null;
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(int id, String author) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null && post.getAuthor().equals(author)) {
            postRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("삭제 권한이 없습니다");

        }
    }
}