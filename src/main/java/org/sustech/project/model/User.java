package org.sustech.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer reputation;
    private Integer numQuestionsAnswered;
    private Integer numAnswersPosted;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReputation() {
        return reputation;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    public Integer getNumQuestionsAnswered() {
        return numQuestionsAnswered;
    }

    public void setNumQuestionsAnswered(Integer numQuestionsAnswered) {
        this.numQuestionsAnswered = numQuestionsAnswered;
    }

    public Integer getNumAnswersPosted() {
        return numAnswersPosted;
    }

    public void setNumAnswersPosted(Integer numAnswersPosted) {
        this.numAnswersPosted = numAnswersPosted;
    }
}

