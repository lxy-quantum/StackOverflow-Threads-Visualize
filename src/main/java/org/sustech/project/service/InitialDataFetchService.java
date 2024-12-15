package org.sustech.project.service;

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
import java.util.ArrayList;
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
                                   UserRepository userRepository, CommentRepository commentRepository) {
        this.restTemplate = new RestTemplate();
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        headers = new HttpHeaders();
        headers.set("User-Agent", "izumion/1.0 (Contact: izuEmail@example.com)");
        entity = new HttpEntity<>(headers);
    }

    private static final String BASE_URL = "https://api.stackexchange.com/2.3";
    String accessToken = "kYNWefr308yMgXcKtQU4iQ))";
    String apiKey = "rl_kEryLkUt66aLf5kW9joBLHUfj";
    private static final int PAGE_SIZE = 100;

    StringBuilder answerIds = new StringBuilder();
    StringBuilder commentIds = new StringBuilder();
    StringBuilder userIds = new StringBuilder();

    int totalAnswerCnt = 0;
    int totalCommentCnt = 0;
    int totalUserCnt = 0;

    public void fetchThreads(int totalCount) {
        int pages = (int) Math.ceil((double) totalCount / PAGE_SIZE);
        int fetchedCount = 0;

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
                    question.setScore(((Number) questionData.get("score")).intValue());
                    question.setViewCount(((Number) questionData.get("view_count")).intValue());
                    question.setUpVoteCount(((Number) questionData.get("up_vote_count")).intValue());
                    question.setDownVoteCount(((Number) questionData.get("down_vote_count")).intValue());
                    int commentCount = ((Number) questionData.get("comment_count")).intValue();
                    question.setCommentCount(commentCount);
                    question.setFavoriteCount(((Number) questionData.get("favorite_count")).intValue());
                    question.setCreationDate(LocalDateTime.ofEpochSecond(
                            ((Number) questionData.get("creation_date")).longValue(), 0, ZoneOffset.UTC));

                    // get owner
                    Map<String, Object> ownerData = (Map<String, Object>) questionData.get("owner");
                    if (ownerData.containsKey("user_id")) {
                        long ownerUserId = ((Number) ownerData.get("user_id")).longValue();
                        question.setOwnerUserId(ownerUserId);
//                        fetchUser(ownerUserId);
                        totalUserCnt++;
                        userIds.append(ownerUserId).append(";");
                    } else {
                        // "user_type": "does_not_exist"
                        continue;
                    }

                    // get last editor
                    Map<String, Object> editorData = (Map<String, Object>) questionData.get("last_editor");
                    if (!(editorData == null)) {
                        if (ownerData.containsKey("user_id")) {
                            long editorUserId = ((Number) editorData.get("user_id")).longValue();
                            question.setLastEditorId(editorUserId);
//                        fetchUser(editorUserId);
                            totalUserCnt++;
                            userIds.append(editorUserId).append(";");
                        } else {
                            // "user_type": "does_not_exist"
                            question.setLastEditorId(null);
                        }
                    } else {
                        question.setLastEditorId(null);
                    }


                    if (answerCount > 0) {
                        List<Map<String, Object>> answers = (List<Map<String, Object>>) questionData.get("answers");
                        for (Map<String, Object> answerData : answers) {
//                            Answer answer = new Answer();
                            long answerId = ((Number) answerData.get("answer_id")).longValue();
                            totalAnswerCnt++;
                            answerIds.append(answerId).append(";");
                            Map<String, Object> answerOwnerData = (Map<String, Object>) answerData.get("owner");
                            if (answerOwnerData.containsKey("user_id")) {
                                long answerOwnerUserId = ((Number) answerOwnerData.get("user_id")).longValue();
//                                answer.setOwnerUserId(answerOwnerUserId);
//                                fetchUser(answerOwnerUserId);
                                totalUserCnt++;
                                userIds.append(answerOwnerUserId).append(";");
                            }
                        }
                    }

                    if (commentCount > 0) {
                        List<Map<String, Object>> comments = (List<Map<String, Object>>) questionData.get("comments");
                        for (Map<String, Object> commentData : comments) {
//                            Comment comment = new Comment();
                            long commentId = ((Number) commentData.get("comment_id")).longValue();
                            totalCommentCnt++;
                            commentIds.append(commentId).append(";");
                            Map<String, Object> commentOwnerData = (Map<String, Object>) commentData.get("owner");
                            if (commentOwnerData.containsKey("user_id")) {
                                long commentOwnerUserId = ((Number) commentOwnerData.get("user_id")).longValue();
                                totalUserCnt++;
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

        fetchAnswers(totalAnswerCnt);
        fetchComments(totalCommentCnt);
        fetchUsers(totalUserCnt);

        System.out.println("Fetched total " + fetchedCount + " threads.");
    }

    private void fetchUsers(int totalCount) {
        int pages = (int) Math.ceil((double) totalCount / PAGE_SIZE);
        int fetchedCount = 0;
        userIds.delete(userIds.length() - 1, userIds.length());
        List<String> idGroups = splitIdString(userIds.toString(), 50);

        for (String userIds : idGroups) {
            try {
                String url = String.format("%s/users/%s?page=1&pagesize=100&order=desc&sort=reputation&site=stackoverflow&filter=!*Mg4PjfXdyMcuyW.&key=%s",
                        BASE_URL, userIds, apiKey);

                ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
                List<Map<String, Object>> users = (List<Map<String, Object>>) response.getBody().get("items");

                if (users.isEmpty()) {
                    break;
                }
                for (Map<String, Object> userData : users) {
                    User user = new User();

                    user.setUserId(((Number) userData.get("user_id")).longValue());
                    user.setDisplayName((String) userData.get("display_name"));
                    if (userData.containsKey("reputation")) {
                        user.setReputation(((Number) userData.get("reputation")).intValue());
                    } else {
                        user.setReputation(0);
                    }
                    if (userData.containsKey("accept_rate")) {
                        user.setAcceptRate(((Number) userData.get("accept_rate")).intValue());
                    } else {
                        user.setAcceptRate(0);
                    }
                    user.setEmployee((Boolean) userData.get("is_employee"));
                    if (userData.containsKey("location")) {
                        user.setLocation((String) userData.get("location"));
                    } else {
                        user.setLocation("unknown");
                    }
                    if (userData.containsKey("view_count")) {
                        user.setViewCount(((Number) userData.get("view_count")).intValue());
                    } else {
                        user.setViewCount(0);
                    }
                    if (userData.containsKey("up_vote_count")) {
                        user.setUpVoteCount(((Number) userData.get("up_vote_count")).intValue());
                    } else {
                        user.setUpVoteCount(0);
                    }
                    if (userData.containsKey("down_vote_count")) {
                        user.setDownVoteCount(((Number) userData.get("down_vote_count")).intValue());
                    } else {
                        user.setDownVoteCount(0);
                    }
                    if (userData.containsKey("question_count")) {
                        user.setQuestionCount(((Number) userData.get("question_count")).intValue());
                    } else {
                        user.setQuestionCount(0);
                    }
                    if (userData.containsKey("answer_count")) {
                        user.setAnswerCount(((Number) userData.get("answer_count")).intValue());
                    } else {
                        user.setAnswerCount(0);
                    }

                    // get badges
                    Map<String, Object> badgeData = (Map<String, Object>) userData.get("badge_counts");
                    user.setBronze(((Number) badgeData.get("bronze")).intValue());
                    user.setSilver(((Number) badgeData.get("silver")).intValue());
                    user.setGold(((Number) badgeData.get("gold")).intValue());

                    userRepository.save(user);

//                    fetchedCount++;
//                    if (fetchedCount >= totalCount) {
//                        System.out.println("Fetched " + fetchedCount + " answers.");
//                        return;
//                    }
                }
//                Thread.sleep(10);
            } catch (Exception e) {
                System.err.println("Error fetching users: " + e.getMessage());
            }
        }
    }

    private void fetchComments(int totalCount) {
        int pages = (int) Math.ceil((double) totalCount / PAGE_SIZE);
        int fetchedCount = 0;
        commentIds.delete(commentIds.length() - 1, commentIds.length());
        List<String> idGroups = splitIdString(commentIds.toString(), 100);

        for (String commentIds : idGroups) {
            try {
                String url = String.format("%s/comments/%s?page=1&pagesize=100&order=desc&sort=creation&site=stackoverflow&filter=!6WPIomp7VPVB4&access_token=%s&key=%s",
                        BASE_URL, commentIds, accessToken, apiKey);

                ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
                List<Map<String, Object>> comments = (List<Map<String, Object>>) response.getBody().get("items");

                if (comments.isEmpty()) {
                    break;
                }
                for (Map<String, Object> commentData : comments) {
                    Comment comment = new Comment();

                    comment.setCommentId(((Number) commentData.get("comment_id")).longValue());

                    // get owner
                    Map<String, Object> ownerData = (Map<String, Object>) commentData.get("owner");
                    if (ownerData.containsKey("user_id")) {
                        long ownerUserId = ((Number) ownerData.get("user_id")).longValue();
                        comment.setOwnerUserId(ownerUserId);
                    } else {
                        comment.setOwnerUserId(null);
                    }

                    comment.setPostType((String) commentData.get("post_type"));
                    comment.setPostId(((Number) commentData.get("post_id")).longValue());
                    comment.setBody((String) commentData.get("body"));
                    comment.setCreationDate(LocalDateTime.ofEpochSecond(
                            ((Number) commentData.get("creation_date")).longValue(), 0, ZoneOffset.UTC));
                    comment.setScore(((Number) commentData.get("score")).intValue());
                    comment.setUpvoted((Boolean) commentData.get("upvoted"));

                    commentRepository.save(comment);

//                    fetchedCount++;
//                    if (fetchedCount >= totalCount) {
//                        System.out.println("Fetched " + fetchedCount + " answers.");
//                        return;
//                    }
                }
            } catch (Exception e) {
                System.err.println("Error fetching comments: " + e.getMessage());
            }
        }
    }

    private void fetchAnswers(int totalCount) {
//        int pages = (int) Math.ceil((double) totalCount / PAGE_SIZE);
//        int fetchedCount = 0;
        answerIds.delete(answerIds.length() - 1, answerIds.length());
        List<String> idGroups = splitIdString(answerIds.toString(), 100);

        for (String answerIds : idGroups) {
            try {
                String url = String.format("%s/answers/%s?page=1&pagesize=100&order=desc&sort=creation&site=stackoverflow&filter=!)sPzvw_P7xc769G0GmaM&access_token=%s&key=%s",
                        BASE_URL, answerIds, accessToken, apiKey);

                ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
                List<Map<String, Object>> answers = (List<Map<String, Object>>) response.getBody().get("items");

                if (answers.isEmpty()) {
                    break;
                }
                for (Map<String, Object> answerData : answers) {
                    Answer answer = new Answer();

                    answer.setAnswerId(((Number) answerData.get("answer_id")).longValue());
                    answer.setQuestionId(((Number) answerData.get("question_id")).longValue());

                    // get owner
                    Map<String, Object> ownerData = (Map<String, Object>) answerData.get("owner");
                    if (ownerData.containsKey("user_id")) {
                        long ownerUserId = ((Number) ownerData.get("user_id")).longValue();
                        answer.setOwnerUserId(ownerUserId);
                    } else {
                        answer.setOwnerUserId(null);
                    }

                    answer.setTitle((String) answerData.get("title"));
                    answer.setBody((String) answerData.get("body"));
                    int commentCount = ((Number) answerData.get("comment_count")).intValue();
                    answer.setCommentCount(commentCount);
                    answer.setIsAccepted((Boolean) answerData.get("is_accepted"));
                    answer.setUpVoteCount(((Number) answerData.get("up_vote_count")).intValue());
                    answer.setDownVoteCount(((Number) answerData.get("down_vote_count")).intValue());
                    answer.setScore(((Number) answerData.get("score")).intValue());
                    answer.setCreationDate(LocalDateTime.ofEpochSecond(
                            ((Number) answerData.get("creation_date")).longValue(), 0, ZoneOffset.UTC));

                    if (commentCount > 0) {
                        List<Map<String, Object>> comments = (List<Map<String, Object>>) answerData.get("comments");
                        for (Map<String, Object> commentData : comments) {
                            long commentId = ((Number) commentData.get("comment_id")).longValue();
                            totalCommentCnt++;
                            commentIds.append(commentId).append(";");
                            Map<String, Object> commentOwnerData = (Map<String, Object>) commentData.get("owner");
                            if (commentOwnerData.containsKey("user_id")) {
                                long commentOwnerUserId = ((Number) commentOwnerData.get("user_id")).longValue();
                                totalUserCnt++;
                                userIds.append(commentOwnerUserId).append(";");
                            }
                        }
                    }

                    answerRepository.save(answer);
                }
            } catch (Exception e) {
                System.err.println("Error fetching answers: " + e.getMessage());
            }
        }
    }

    private List<String> splitIdString(String str, int groupSize) {
        String[] ids = str.split(";");

        List<String> result = new ArrayList<>();

        StringBuilder group = new StringBuilder();
        for (int i = 0; i < ids.length; i++) {
            if (!group.isEmpty()) {
                group.append(";");
            }
            group.append(ids[i]);

            if ((i + 1) % groupSize == 0 || i == ids.length - 1) {
                result.add(group.toString());
                group.setLength(0);
            }
        }

        return result;
    }
}


