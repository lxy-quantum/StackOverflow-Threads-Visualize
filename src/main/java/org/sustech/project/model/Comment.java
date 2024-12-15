package org.sustech.project.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "owner_user_id")
    private Long ownerUserId;

    @Column(name = "post_type")
    private String postType;

    @Column(name = "post_id")
    private Long postId;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    private Integer score;

    private Boolean upvoted;


    // Getters and setters
    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long id) {
        this.commentId = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getUpvoted() {
        return upvoted;
    }

    public void setUpvoted(Boolean upvoted) {
        this.upvoted = upvoted;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(Long userId) {
        this.ownerUserId = userId;
    }
}

