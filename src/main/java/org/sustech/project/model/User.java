package org.sustech.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "display_name")
    private String displayName;

    private Integer reputation;

    @Column(name = "accepet_rate")
    private Integer acceptRate;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "up_vote_count")
    private Integer upVoteCount;

    @Column(name = "down_vote_count")
    private Integer downVoteCount;

    @Column(name = "question_count")
    private Integer questionCount;

    @Column(name = "answer_count")
    private Integer answerCount;

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long id) {
        this.userId = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getAcceptRate() {
        return acceptRate;
    }

    public void setAcceptRate(Integer acceptRate) {
        this.acceptRate = acceptRate;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
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

    public Integer getReputation() {
        return reputation;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer numQuestionsAnswered) {
        this.questionCount = numQuestionsAnswered;
    }

    public Integer getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(Integer numAnswersPosted) {
        this.answerCount = numAnswersPosted;
    }
}

