package com.epam.testsystem.github.service;

import com.epam.testsystem.github.dao.TaskDao;
import com.epam.testsystem.github.dao.UserDao;
import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.TaskStatus;
import com.epam.testsystem.github.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * github_test
 * Created on 05.07.17.
 */
@Service
@RequiredArgsConstructor
public class CheckGitHubService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubStatusResolver.class);

    private static final int SECOND = 1000;

    private static final String OWNER = "epamtestsystem";

    private static final String REPO = "java_knowledge";

    private final GitHubStatusResolver gitHubStatusResolver;

    private final TaskDao taskDao;

    private final UserDao userDao;

    @Scheduled(fixedDelay = 5 * SECOND)
    @Transactional
    public void check() {
        for (Task t : taskDao.findAllInProgress()) {
            final Optional<User> user = userDao.findById(t.getUserId());
            if (!user.isPresent()) {
                LOGGER.info("User with id = {} is no present", t.getUserId());
            } else {
                final User presentedUser = user.get();
                try {
                    final boolean userResult = gitHubStatusResolver.getUserResult(presentedUser.getGithubNick(), OWNER, REPO);
                    taskDao.setResultById(presentedUser.getId(), t.getId(), userResult);
                    LOGGER.info("Task {} from User {} checked", t.getId(), t.getUserId());
                } catch (Exception e) {
                    taskDao.setResultById(presentedUser.getId(), t.getId(), false);
                    LOGGER.info("Task {} from User {} crashed\n {}", t.getId(), t.getUserId(), e.getMessage());
                }
            }
        }
    }
}
