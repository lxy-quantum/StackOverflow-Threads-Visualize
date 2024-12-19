package org.sustech.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sustech.project.dto.TopicFrequencyDTO;
import org.sustech.project.service.DataAnalysisService;

import java.util.List;

@RestController
@RequestMapping("/api/data")
public class DataRestController {

    @Autowired
    private DataAnalysisService dataAnalysisService;

    @GetMapping
    public List<TopicFrequencyDTO> getTopNTopics() {
        return dataAnalysisService.getTopNJavaTopics();
    }

    @GetMapping(path = "{tag}")
    public TopicFrequencyDTO fetchQuestions() {
        return null;
    }
}

