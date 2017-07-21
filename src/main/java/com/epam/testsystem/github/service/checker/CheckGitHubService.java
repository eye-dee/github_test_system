package com.epam.testsystem.github.service.checker;

import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.service.log.GitHubStatusResolver;
import com.epam.testsystem.github.service.task.TaskService;
import com.epam.testsystem.github.service.user.UserService;
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
    private final TaskService taskService;
    private final UserService userService;

    @Scheduled(fixedDelay = 5 * SECOND)
    @Transactional
    public void check() {
        for (Task t : taskService.findAllInProgress()) {
            final Optional<User> user = userService.findById(t.getUserId());
            if (!user.isPresent()) {
                LOGGER.info("User with id = {} is no present", t.getUserId());
            } else {
                final User presentedUser = user.get();
                try {
                    // TODO: 7/18/2017 Resolve logs
                    final boolean userResult = gitHubStatusResolver.getUserResult(presentedUser.getGitNick(), OWNER, REPO);
                    taskService.setResultById(presentedUser.getId(), t.getId(), userResult, "{}");
                    LOGGER.info("Task {} from User {} checked", t.getId(), t.getUserId());
                } catch (Exception e) {
                    taskService.setResultById(presentedUser.getId(), t.getId(), false, "{}");
                    LOGGER.info("Task {} from User {} crashed\n {}", t.getId(), t.getUserId(), e.getMessage());
                }
            }
        }
    }
}
