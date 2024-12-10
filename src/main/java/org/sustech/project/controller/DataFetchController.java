package org.sustech.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sustech.project.service.StackOverflowDataService;

@RestController
@RequestMapping("/api/data")
public class DataFetchController {
    @Autowired
    private StackOverflowDataService dataService;

    @GetMapping("/fetch-questions")
    public String fetchQuestions() {
        dataService.fetchAndStoreQuestions(1000);
        return "Questions fetched and stored.";
    }

    @GetMapping("/fetch-answers/{questionId}")
    public String fetchAnswers(@PathVariable Long questionId) {
        dataService.fetchAndStoreAnswers(questionId);
        return "Answers fetched and stored for question: " + questionId;
    }
}

