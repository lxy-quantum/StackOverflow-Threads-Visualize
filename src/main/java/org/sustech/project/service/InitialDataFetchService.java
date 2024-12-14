package org.sustech.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
    public InitialDataFetchService(QuestionRepository questionRepository, AnswerRepository answerRepository,
                                   UserRepository userRepository) {
        this.restTemplate = new RestTemplate();
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0");
        entity = new HttpEntity<>(headers);
    }

    private static final String BASE_URL = "https://api.stackexchange.com/2.3";
    String accessToken = "S4mugHKdRMd65eLkyWFBAA))";
    String apiKey = "rl_kEryLkUt66aLf5kW9joBLHUfj";
    private static final int PAGE_SIZE = 100;

    public void fetchQuestions(int totalCount) {
        int pages = (int) Math.ceil((double) totalCount / PAGE_SIZE);
        int fetchedCount = 0;

        int totalAnswerCnt = 0;
        int totalCommentCnt = 0;
        int totalUserCnt = 0;

        StringBuilder answerIds = new StringBuilder();
        StringBuilder commentIds = new StringBuilder();
        StringBuilder userIds = new StringBuilder();

        for (int page = 1; page <= pages; page++) {
            try {
                String url = String.format("%s/questions?order=desc&sort=activity&tagged=java&site=stackoverflow" +
                                "&page=%d&pagesize=%d&filter=!T3Audpcl0.kAsOUkde&access_token=%s&key=%s",
                        BASE_URL, page, PAGE_SIZE, accessToken, apiKey);

                ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
                List<Map<String, Object>> questions = (List<Map<String, Object>>) response.getBody().get("items");

                for (Map<String, Object> questionData : questions) {
                    Question question = new Question();
                    question.setQuestionId(((Number) questionData.get("question_id")).longValue());
                    question.setQuestionId((Long) questionData.get("question_id"));

                    List<String> tags = (List<String>) questionData.get("tags");
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String tag : tags) {
                        stringBuilder.append(tag).append(", ");
                    }
                    stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                    question.setTags(stringBuilder.toString());

                    question.setTitle((String) questionData.get("title"));
                    question.setBody((String) questionData.get("body"));
                    int answerCount = ((Number) questionData.get("answer_count")).intValue();
                    question.setAnswerCount(answerCount);
                    question.setScore((Integer) questionData.get("score"));
                    question.setViewCount((Integer) questionData.get("view_count"));
                    question.setUpVoteCount((Integer) questionData.get("up_vote_count"));
                    question.setDownVoteCount((Integer) questionData.get("down_vote_count"));
                    int commentCount = ((Number) questionData.get("comment_count")).intValue();
                    question.setCommentCount(commentCount);
                    question.setFavoriteCount((Integer) questionData.get("favorite_count"));
                    question.setCreationDate(LocalDateTime.ofEpochSecond(
                            ((Number) questionData.get("creation_date")).longValue(), 0, ZoneOffset.UTC));

                    // get owner
                    Map<String, Object> ownerData = (Map<String, Object>) questionData.get("owner");
                    if (ownerData.containsKey("user_id")) {
                        long ownerUserId = ((Number) ownerData.get("user_id")).longValue();
                        question.setOwnerUserId(ownerUserId);
//                        fetchUser(ownerUserId);
                        userIds.append(ownerUserId).append(";");
                    } else {
                        // "user_type": "does_not_exist"
                        continue;
                    }

                    // get last editor
                    Map<String, Object> editorData = (Map<String, Object>) questionData.get("last_editor");
                    if (ownerData.containsKey("user_id")) {
                        long editorUserId = ((Number) editorData.get("user_id")).longValue();
                        question.setLastEditorId(editorUserId);
//                        fetchUser(editorUserId);
                        userIds.append(editorUserId).append(";");
                    } else {
                        // "user_type": "does_not_exist"
                        question.setLastEditorId(null);
                    }

                    if (answerCount > 0) {
                        List<Map<String, Object>> answers = (List<Map<String, Object>>) questionData.get("answers");
                        for (Map<String, Object> answerData : answers) {
//                            Answer answer = new Answer();
                            long answerId = ((Number) answerData.get("answer_id")).longValue();
                            answerIds.append(answerId).append(";");
                            Map<String, Object> answerOwnerData = (Map<String, Object>) answerData.get("owner");
                            if (answerOwnerData.containsKey("user_id")) {
                                long answerOwnerUserId = ((Number) answerOwnerData.get("user_id")).longValue();
//                                answer.setOwnerUserId(answerOwnerUserId);
//                                fetchUser(answerOwnerUserId);
                                userIds.append(answerOwnerUserId).append(";");
                            }
                        }
                    }

                    if (commentCount > 0) {
                        List<Map<String, Object>> comments = (List<Map<String, Object>>) questionData.get("comments");
                        for (Map<String, Object> commentData : comments) {
//                            Comment comment = new Comment();
                            long commentId = ((Number) commentData.get("comment_id")).longValue();
                            commentIds.append(commentId).append(";");
                            Map<String, Object> commentOwnerData = (Map<String, Object>) commentData.get("owner");
                            if (commentOwnerData.containsKey("user_id")) {
                                long commentOwnerUserId = ((Number) commentOwnerData.get("user_id")).longValue();
                                userIds.append(commentOwnerUserId).append(";");
                            }
                        }
                    }

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
                System.err.println("Error fetching questions for page " + page + ": " + e.getMessage());
            }
        }

        System.out.println("Fetched total " + fetchedCount + " questions.");
    }

    private void fetchUser(Long userId) {
        String url = String.format("%s/users/%d?site=stackoverflow&filter=!*Mg4PjfXdyMcuyW&key=%s", BASE_URL, userId, apiKey);
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> userData = ((List<Map<String, Object>>) response.getBody().get("items")).get(0);

        User user = new User();
        user.setUserId(userId);
        user.setDisplayName((String) userData.get("display_name"));
        user.setReputation((Integer) userData.get("reputation"));
        user.setAcceptRate((Integer) userData.get("accept_rate"));
        user.setViewCount((Integer) userData.get("view_count"));
        user.setUpVoteCount((Integer) userData.get("up_vote_count"));
        user.setDownVoteCount((Integer) userData.get("down_vote_count"));
        user.setQuestionCount((Integer) userData.get("question_count"));
        user.setAnswerCount((Integer) userData.get("answer_count"));

        userRepository.save(user);
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
            fetchUser(((Number) userData.get("user_id")).longValue());
//            comment.setUserId(commenter.getId());

            commentRepository.save(comment);
        }
    }

    private void fetchAnswersForQuestion(Long questionId, Map<String, Object> questionData) {
        List<Map<String, Object>> answersData = (List<Map<String, Object>>) questionData.get("answers");

        for (Map<String, Object> answerData : answersData) {
            Answer answer = new Answer();
            answer.setQuestionId(questionId);
            answer.setBody((String) answerData.get("body"));
            answer.setUpVoteCount((Integer) answerData.getOrDefault("up_vote_count", 0));
            answer.setDownVoteCount((Integer) answerData.getOrDefault("down_vote_count", 0));
            answer.setCreationDate(LocalDateTime.ofEpochSecond(
                    ((Number) answerData.get("creation_date")).longValue(), 0, ZoneOffset.UTC));

            Map<String, Object> userData = (Map<String, Object>) answerData.get("owner");
            fetchUser(((Number) userData.get("user_id")).longValue());
//            answer.setUserId(answerer.getId());

            fetchCommentsForPost(answer.getAnswerId(), answerData);

            answerRepository.save(answer);
        }
    }
}


