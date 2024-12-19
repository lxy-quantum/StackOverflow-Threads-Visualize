package org.sustech.project.controller;

import ch.qos.logback.core.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.sustech.project.dto.TopicEngagementDTO;
import org.sustech.project.dto.TopicFrequencyDTO;
import org.sustech.project.service.DataAnalysisService;

import java.util.List;

@Controller
public class VisualizeController {

    @Autowired
    DataAnalysisService dataAnalysisService;

    @GetMapping("/topic-freq")
    public List<TopicFrequencyDTO> topicFreq(Model model) {
        return dataAnalysisService.getTopNJavaTopics();
    }

    @GetMapping("/engagement")
    public List<TopicEngagementDTO> getTopEngagedTopicsByReputation() {
        return dataAnalysisService.getTopEngagedTopics();
    }

}
