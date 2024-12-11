package org.sustech.project.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "questions")
public class Question {
    @Id
    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "owner_user_id")
    private Long ownerUserId;

    private String tags;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(name = "last_editor_id")
    private Long lastEditorId;

    @Column(name = "answer_count")
    private Integer answerCount;

    private Integer score;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "up_vote_count")
    private Integer upVoteCount;

    @Column(name = "down_vote_count")
    private Integer downVoteCount;

    @Column(name = "comment_count")
    private Integer commentCount;

    @Column(name = "favoriteCount")
    private Integer favoriteCount;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    // Getters and setters
    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long question_id) {
        this.questionId = question_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Long getLastEditorId() {
        return lastEditorId;
    }

    public void setLastEditorId(Long lastEditorId) {
        this.lastEditorId = lastEditorId;
    }

    public Integer getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public void setUpVoteCount(Integer upvotes) {
        this.upVoteCount = upvotes;
    }

    public void setDownVoteCount(Integer downvotes) {
        this.downVoteCount = downvotes;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setViewCount(Integer views) {
        this.viewCount = views;
    }

    public void setOwnerUserId(Long userId) {
        this.ownerUserId = userId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Integer getUpVoteCount() {
        return upVoteCount;
    }

    public Integer getDownVoteCount() {
        return downVoteCount;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public Long getOwnerUserId() {
        return ownerUserId;
    }
}

