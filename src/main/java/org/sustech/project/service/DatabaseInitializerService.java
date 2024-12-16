package org.sustech.project.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sustech.project.model.UserGroupStatus;
import org.sustech.project.repository.QuestionRepository;
import org.sustech.project.repository.UserGroupStatusRepository;

import java.util.List;

@Service
public class DatabaseInitializerService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserGroupStatusRepository userGroupStatusRepository;

    @Autowired
    private InitialDataFetchService initialDataFetchService;

    @PostConstruct
    public void initializeDatabase() throws InterruptedException {
        long questionCount = questionRepository.count();
        if (questionCount == 0) {
            System.out.println("Initializing database with Stack Overflow data...");
            initialDataFetchService.fetchThreads(1300);
            System.out.println("Questions, Answers and Comments Data initialization completed.");
        } else {
            List<UserGroupStatus> falseGroups = userGroupStatusRepository.findByStatusFalse();
            if (!falseGroups.isEmpty()) {
                System.out.println("Fetching users data.");
                int groupCount = 0;
//                for (UserGroupStatus groupStatus : falseGroups) {
//                    String groupIds = groupStatus.getGroupIds();
//                    try {
//                        initialDataFetchService.fetchUsers(groupIds);
//                        userGroupStatusRepository.updateStatusToTrueByGroupId(groupIds);
//                        groupCount++;
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                        userGroupStatusRepository.updateStatusToFalseByGroupId(groupIds);
//                    }
//                    Thread.sleep(12000);
//                }

                String groupIds = falseGroups.get(0).getGroupIds();
                try {
                    initialDataFetchService.fetchUsers(groupIds);
                    userGroupStatusRepository.updateStatusToTrueByGroupId(groupIds);
                    groupCount++;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    userGroupStatusRepository.updateStatusToFalseByGroupId(groupIds);
                }
                System.out.println("Fetched " + groupCount + " user group(s).");
            } else {
                System.out.println("Database already initialized. Skipping data fetch.");
            }
        }
    }
}

