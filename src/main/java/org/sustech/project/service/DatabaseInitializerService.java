package org.sustech.project.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sustech.project.repository.QuestionRepository;

@Service
public class DatabaseInitializerService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private InitialDataFetchService initialDataFetchService;

    @PostConstruct
    public void initializeDatabase() {
        long questionCount = questionRepository.count();
        if (questionCount == 0) {
            System.out.println("Initializing database with Stack Overflow data...");
            initialDataFetchService.fetchQuestions(100);
            System.out.println("Database initialization completed.");
        } else {
            System.out.println("Database already initialized. Skipping data fetch.");
        }
    }
}

