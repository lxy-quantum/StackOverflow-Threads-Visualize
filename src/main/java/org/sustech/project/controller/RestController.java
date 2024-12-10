package org.sustech.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.sustech.project.service.InitialDataFetchService;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/data")
public class RestController {
    @Autowired
    private InitialDataFetchService dataService;

//    @GetMapping("/fetch-questions")
//    public String fetchQuestions() {
//        dataService.fetchAndStoreQuestions(1000);
//        return "Questions fetched and stored.";
//    }
}

