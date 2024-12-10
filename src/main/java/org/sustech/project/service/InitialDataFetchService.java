package org.sustech.project.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.sustech.project.model.Answer;
import org.sustech.project.model.Comment;
import org.sustech.project.model.Question;
import org.sustech.project.model.User;
import org.sustech.project.repository.AnswerRepository;
import org.sustech.project.repository.CommentRepository;
import org.sustech.project.repository.QuestionRepository;
import org.sustech.project.repository.UserRepository;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

@Service
public class InitialDataFetchService {

    private RestTemplate restTemplate;

    private QuestionRepository questionRepository;

    private AnswerRepository answerRepository;

    private UserRepository userRepository;

    private CommentRepository commentRepository;

    private HttpHeaders headers;
    private HttpEntity<String> entity;

    @Autowired
    public InitialDataFetchService(RestTemplate restTemplate, QuestionRepository questionRepository,
                                   AnswerRepository answerRepository, UserRepository userRepository) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy_host", 8080)));
        this.restTemplate = new RestTemplate(factory);
        this.restTemplate = new RestTemplate();
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0");
//        headers.set("Accept", "*/*");
        entity = new HttpEntity<>(headers);
    }

    private static final String BASE_URL = "https://api.stackexchange.com/2.3";
    String apiKey = ""; //""rl_kEryLkUt66aLf5kW9joBLHUfj";
    private static final int PAGE_SIZE = 100;

//    @Transactional
    public void fetchQuestions(int totalCount) {
        int pages = (int) Math.ceil((double) totalCount / PAGE_SIZE);
        int fetchedCount = 0;

        for (int page = 1; page <= pages; page++) {
            try {
                String url = String.format("%s/questions?order=desc&sort=activity&tagged=java&site=stackoverflow&key=%s&page=%d&pagesize=%d&filter=withbody",
                        BASE_URL, apiKey, 1, PAGE_SIZE);

                ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
                List<Map<String, Object>> questions = (List<Map<String, Object>>) response.getBody().get("items");

                for (Map<String, Object> questionData : questions) {
                    Question question = new Question();
//                    question.setId(((Number) questionData.get("question_id")).longValue());
                    question.setTitle((String) questionData.get("title"));
                    question.setBody((String) questionData.get("body"));
//                    question.setUpvotes((Integer) questionData.getOrDefault("up_vote_count", 0));
//                    question.setDownvotes((Integer) questionData.getOrDefault("down_vote_count", 0));
                    question.setUpvotes(10);
                    question.setDownvotes(10);
                    question.setCreationDate(LocalDateTime.ofEpochSecond(
                            ((Number) questionData.get("creation_date")).longValue(), 0, ZoneOffset.UTC));
                    question.setViews((Integer) questionData.getOrDefault("view_count", 0));

                    // get the owner of question
                    Map<String, Object> ownerData = (Map<String, Object>) questionData.get("owner");
                    question.setUserId(((Number) ownerData.get("user_id")).longValue());
                    // fetch and store the data of owner as User
//                    User questionOwner = fetchUser(((Number) ownerData.get("user_id")).longValue());
//                    question.setUserId(questionOwner.getId());  // set the user_id for the question

                    questionRepository.save(question);

                    // fetch and store the data of comments as Comment
//                    fetchCommentsForPost(question.getId(), questionData);

                    // fetch and store the data of answers as Answer
//                    fetchAnswersForQuestion(question.getId(), questionData);

                    fetchedCount++;

                    if (fetchedCount >= totalCount) {
                        System.out.println("Fetched " + fetchedCount + " questions.");
                        return;
                    }
                }
            } catch (Exception e) {
                System.err.println("Error fetching questions for page " + 1 + ": " + e.getMessage());
            }
        }

        System.out.println("Fetched total " + fetchedCount + " questions.");
    }

    private User fetchUser(Long userId) {
        String url = String.format("%s/users/%d?site=stackoverflow&key=%s", BASE_URL, userId, apiKey);
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> userData = ((List<Map<String, Object>>) response.getBody().get("items")).get(0);

        User user = new User();
        user.setId(((Number) userData.get("user_id")).longValue());
        user.setReputation((Integer) userData.get("reputation"));
        user.setNumQuestionsAnswered((Integer) userData.get("questions"));
        user.setNumAnswersPosted((Integer) userData.get("answers"));

//        userRepository.save(user);
        return user;
    }

    private void fetchCommentsForPost(Long postId, Map<String, Object> postData) {
        String url = String.format("%s/posts/%d/comments?order=desc&sort=creation&site=stackoverflow&key=%s",
                BASE_URL, postId, apiKey);

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        List<Map<String, Object>> comments = (List<Map<String, Object>>) response.getBody().get("items");

        for (Map<String, Object> commentData : comments) {
            Comment comment = new Comment();
            comment.setPostId(postId);
            comment.setBody((String) commentData.get("body"));
            comment.setCreationDate(LocalDateTime.ofEpochSecond(
                    ((Number) commentData.get("creation_date")).longValue(), 0, ZoneOffset.UTC));

            // 获取评论者
            Map<String, Object> userData = (Map<String, Object>) commentData.get("user");
            User commenter = fetchUser(((Number) userData.get("user_id")).longValue());
            comment.setUserId(commenter.getId());

            commentRepository.save(comment);
        }
    }

    // 获取并存储回答数据
    private void fetchAnswersForQuestion(Long questionId, Map<String, Object> questionData) {
        List<Map<String, Object>> answersData = (List<Map<String, Object>>) questionData.get("answers");

        for (Map<String, Object> answerData : answersData) {
            Answer answer = new Answer();
            answer.setQuestionId(questionId);
            answer.setBody((String) answerData.get("body"));
            answer.setUpvotes((Integer) answerData.getOrDefault("up_vote_count", 0));
            answer.setDownvotes((Integer) answerData.getOrDefault("down_vote_count", 0));
            answer.setCreationDate(LocalDateTime.ofEpochSecond(
                    ((Number) answerData.get("creation_date")).longValue(), 0, ZoneOffset.UTC));

            // 获取回答者
            Map<String, Object> userData = (Map<String, Object>) answerData.get("owner");
            User answerer = fetchUser(((Number) userData.get("user_id")).longValue());
            answer.setUserId(answerer.getId());

            // 获取回答的评论
            fetchCommentsForPost(answer.getId(), answerData);

            answerRepository.save(answer);
        }
    }
}


