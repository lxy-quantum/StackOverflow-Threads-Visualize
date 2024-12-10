package org.sustech.project.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long questionId;
    private String body;
    private Integer upvotes;
    private Integer downvotes;
    private LocalDateTime creationDate;
    private Long userId;
    private Boolean isAccepted;

    // Getters and setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
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

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }

    public Long getId() {
        return id;
    }

    public Long getQuestionId() {
        return questionId;
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

    public Long getUserId() {
        return userId;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }
}

