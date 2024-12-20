package org.sustech.project.controller;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sustech.project.dto.AnswerQualityDTO;
import org.sustech.project.dto.MistakeDTO;
import org.sustech.project.dto.TopicEngagementDTO;
import org.sustech.project.dto.TopicFrequencyDTO;
import org.sustech.project.service.DataAnalysisService;

import java.util.List;

@RestController
@Controller
@CrossOrigin(origins = "*")
public class VisualizeController {

    private final DataAnalysisService dataAnalysisService;

    @Autowired
    public VisualizeController(DataAnalysisService dataAnalysisService) {
        this.dataAnalysisService = dataAnalysisService;
    }

    @GetMapping("/topics/top")
    public List<TopicFrequencyDTO> getTopTopic() {
        return dataAnalysisService.getTopNJavaTopics();
    }

    @GetMapping("/topics/engagement")
    public List<TopicEngagementDTO> getTopEngaged() {
        return dataAnalysisService.getTopEngagedTopics();
    }

    @GetMapping("/mistakes")
    public List<MistakeDTO> getMistakes() {
        return dataAnalysisService.getCommonMistakes();
    }

    @GetMapping("/answers/quality/time")
    public List<AnswerQualityDTO> getQualityByTime() {
        return dataAnalysisService.getAnswerQualityByTime();
    }

    @GetMapping("/answers/quality/reputation")
    public List<AnswerQualityDTO> getQualityByReputation() {
        return dataAnalysisService.getAnswerQualityByReputation();
    }

    @GetMapping("/answers/quality/length")
    public List<AnswerQualityDTO> getQualityByLength() {
        return dataAnalysisService.getAnswerQualityByLength();
    }
}
