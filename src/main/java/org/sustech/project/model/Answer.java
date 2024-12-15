package org.sustech.project.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "answers")
public class Answer {
    @Id
    @Column(name = "answer_id")
    private Long answerId;

    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "owner_user_id")
    private Long ownerUserId;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(name = "comment_count")
    private Integer commentCount;

    @Column(name = "is_accepted")
    private Boolean isAccepted;

    @Column(name = "up_vote_count")
    private Integer upVoteCount;

    @Column(name = "down_vote_count")
    private Integer downVoteCount;

    private Integer score;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    // Getters and setters

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(Long ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Boolean isAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Boolean accepted) {
        isAccepted = accepted;
    }

    public Integer getUpVoteCount() {
        return upVoteCount;
    }

    public void setUpVoteCount(Integer upVoteCount) {
        this.upVoteCount = upVoteCount;
    }

    public Integer getDownVoteCount() {
        return downVoteCount;
    }

    public void setDownVoteCount(Integer downVoteCount) {
        this.downVoteCount = downVoteCount;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}

