package org.sustech.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sustech.project.dto.TopicEngagementDTO;
import org.sustech.project.dto.TopicFrequencyDTO;
import org.sustech.project.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataAnalysisService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<TopicFrequencyDTO> getTopNJavaTopics() {
        List<Object[]> results = questionRepository.findTopNJavaTopics();
        return results.stream()
                .map(result -> new TopicFrequencyDTO((String) result[0], (Double) result[1]))
                .collect(Collectors.toList());
    }

    public List<TopicEngagementDTO> getTopEngagedTopics() {
        List<Object[]> results = questionRepository.findTopEngagedTopics();
        return results.stream()
                .map(result -> new TopicEngagementDTO((String) result[0], (Long) result[1]))
                .collect(Collectors.toList());
    }

}
