package org.sustech.project.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String title;
    @Column(columnDefinition = "TEXT")
    private String body;
    private Integer upvotes;
    private Integer downvotes;
    private LocalDateTime creationDate;
    private Integer views;
    private Long userId;

    // Getters and setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setUpvotes(Integer upvotes) {
        this.upvotes = upvotes;
    }

    public void setDownvotes(Integer downvotes) {
        this.downvotes = downvotes;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Integer getUpvotes() {
        return upvotes;
    }

    public Integer getDownvotes() {
        return downvotes;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Integer getViews() {
        return views;
    }

    public Long getUserId() {
        return userId;
    }
}

