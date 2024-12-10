package org.sustech.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.sustech.project.model.Answer;
import org.sustech.project.model.Question;
import org.sustech.project.repository.AnswerRepository;
import org.sustech.project.repository.CommentRepository;
import org.sustech.project.repository.QuestionRepository;
import org.sustech.project.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

@Service
public class StackOverflowDataService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;

    private static final String BASE_URL = "https://api.stackexchange.com/2.3";

    public void fetchAndStoreQuestions() {
        String url = BASE_URL + "/questions?order=desc&sort=activity&tagged=java&site=stackoverflow";

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        List<Map<String, Object>> questions = (List<Map<String, Object>>) response.getBody().get("items");

        for (Map<String, Object> questionData : questions) {
            Question question = new Question();
            question.setId(((Number) questionData.get("question_id")).longValue());
            question.setTitle((String) questionData.get("title"));
            question.setBody((String) questionData.get("body"));
            question.setUpvotes((Integer) questionData.get("up_vote_count"));
            question.setDownvotes((Integer) questionData.get("down_vote_count"));
            question.setCreationDate(LocalDateTime.ofEpochSecond(
                    ((Number) questionData.get("creation_date")).longValue(), 0, ZoneOffset.UTC));
            question.setViews((Integer) questionData.get("view_count"));
            question.setUserId(((Number) questionData.get("owner")).longValue());

            // save to backend database
            questionRepository.save(question);
        }
    }

    public void fetchAndStoreAnswers(Long questionId) {
        String url = BASE_URL + "/questions/" + questionId + "/answers?order=desc&sort=activity&site=stackoverflow";

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        List<Map<String, Object>> answers = (List<Map<String, Object>>) response.getBody().get("items");

        for (Map<String, Object> answerData : answers) {
            Answer answer = new Answer();
            answer.setId(((Number) answerData.get("answer_id")).longValue());
            answer.setQuestionId(questionId);
            answer.setBody((String) answerData.get("body"));
            answer.setUpvotes((Integer) answerData.get("up_vote_count"));
            answer.setDownvotes((Integer) answerData.get("down_vote_count"));
            answer.setCreationDate(LocalDateTime.ofEpochSecond(
                    ((Number) answerData.get("creation_date")).longValue(), 0, ZoneOffset.UTC));
            answer.setUserId(((Number) answerData.get("owner")).longValue());
            answer.setAccepted((Boolean) answerData.get("is_accepted"));

            answerRepository.save(answer);
        }
    }
}


