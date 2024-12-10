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
}

