package org.sustech.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sustech.project.dto.AnswerQualityDTO;
import org.sustech.project.dto.MistakeDTO;
import org.sustech.project.dto.TopicEngagementDTO;
import org.sustech.project.dto.TopicFrequencyDTO;
import org.sustech.project.repository.AnswerRepository;
import org.sustech.project.repository.QuestionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataAnalysisService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Autowired
    public DataAnalysisService(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    public List<TopicFrequencyDTO> getTopNJavaTopics(int limitValue) {
        List<Object[]> results = questionRepository.findTopNJavaTopics(limitValue);
        return results.stream()
                .map(result -> new TopicFrequencyDTO((String) result[0], (Double) result[1]))
                .collect(Collectors.toList());
    }

    public List<TopicFrequencyDTO> getTopicFrequency(String tag) {
        List<Object[]> results = questionRepository.findTopicFrequency(tag);
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

    public List<MistakeDTO> getCommonMistakes(int limitValue) {
        List<Object[]> results = questionRepository.findCommonMistakes(limitValue);
        return results.stream()
                .map(result -> new MistakeDTO((String) result[0], (Long) result[1]))
                .collect(Collectors.toList());
    }

    public List<MistakeDTO> getMistakeFrequency(String name) {
        List<Object[]> results = questionRepository.findMistakeFrequency(name);
        return results.stream()
                .map(result -> new MistakeDTO((String) result[0], (Long) result[1]))
                .collect(Collectors.toList());
    }

    public List<AnswerQualityDTO> getAnswerQualityByTime() {
        List<Object[]> results = answerRepository.getQualityByTime();
        return results.stream()
                .map(result -> new AnswerQualityDTO((String) result[0], (Double) result[1], (Double) result[2], (Long) result[3]))
                .collect(Collectors.toList());
    }

    public List<AnswerQualityDTO> getAnswerQualityByReputation() {
        List<Object[]> results = answerRepository.getQualityByReputation();
        return results.stream()
                .map(result -> new AnswerQualityDTO((String) result[0], (Double) result[1], (Double) result[2], (Long) result[3]))
                .collect(Collectors.toList());
    }

    public List<AnswerQualityDTO> getAnswerQualityByLength() {
        List<Object[]> results = answerRepository.getQualityByLength();
        return results.stream()
                .map(result -> new AnswerQualityDTO((String) result[0], (Double) result[1], (Double) result[2], (Long) result[3]))
                .collect(Collectors.toList());
    }

}
