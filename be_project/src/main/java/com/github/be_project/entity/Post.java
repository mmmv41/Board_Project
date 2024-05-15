package com.github.be_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 350, nullable = false)
    private String title;
    private String content;

    @Column(nullable = false)
    private int author_Id;

    private int liked;

    @CreationTimestamp
    private LocalDateTime created_At;

    @UpdateTimestamp
    private LocalDateTime updated_At;
}
