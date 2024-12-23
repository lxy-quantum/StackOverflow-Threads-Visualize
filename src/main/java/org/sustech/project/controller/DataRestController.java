package org.sustech.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sustech.project.dto.MistakeDTO;
import org.sustech.project.dto.TopicFrequencyDTO;
import org.sustech.project.service.DataAnalysisService;

import java.util.List;

@RestController
@RequestMapping("/api/stackdata")
public class DataRestController {

    @Autowired
    private DataAnalysisService dataAnalysisService;

    @GetMapping("/topics/topN")
    public List<TopicFrequencyDTO> getTopNTopics(@RequestParam(value = "N") Integer N) {
        return dataAnalysisService.getTopNJavaTopics(N);
    }

    @GetMapping("/topics/{tag}")
    public List<TopicFrequencyDTO> getTopicFrequency(@PathVariable("tag") String tag) {
        return dataAnalysisService.getTopicFrequency(tag);
    }

    @GetMapping("/mistakes/topN")
    public List<MistakeDTO> getTopNMistakes(@RequestParam(value = "N") Integer N) {
        return dataAnalysisService.getCommonMistakes(N);
    }

    @GetMapping("/mistakes/{name}")
    public List<MistakeDTO> getMistakeFrequency(@PathVariable("name") String name) {
        return dataAnalysisService.getMistakeFrequency(name);
    }

}

