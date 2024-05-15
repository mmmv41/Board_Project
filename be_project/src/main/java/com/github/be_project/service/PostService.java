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

    @Transactional(readOnly = true)
    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Post getPost(int id) {
        return postRepository.findById(id).orElse(null);
    }

    @Transactional
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public Post editPost(int id, Post updatedPost) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            post.setTitle(updatedPost.getTitle());
            post.setContent(updatedPost.getContent());
            post.setLiked(updatedPost.getLiked());
            post.setUpdated_At(updatedPost.getUpdated_At());
            return postRepository.save(post);
        }
        return null;
    }

    @Transactional
    public void deletePost(int id) {
        postRepository.deleteById(id);
    }

    @Transactional
    public Post likePost(int id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            post.setLiked(post.getLiked() + 1); // 좋아요 증가
            return postRepository.save(post);
        }
        return null;
    }
}