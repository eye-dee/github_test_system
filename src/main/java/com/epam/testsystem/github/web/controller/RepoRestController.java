package com.epam.testsystem.github.web.controller;

import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.service.repo.RepoService;
import com.epam.testsystem.github.service.task.TaskService;
import com.epam.testsystem.github.service.user.UserService;
import com.epam.testsystem.github.web.mapper.MapperUi;
import com.epam.testsystem.github.web.model.NewRepoUI;
import com.epam.testsystem.github.web.model.TaskUI;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

/**
 * github_test
 * Create on 7/13/2017.
 */

@RestController
@RequestMapping("repo")
@RequiredArgsConstructor
@Validated
public class RepoRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepoRestController.class);
    private final UserService userService;
    private final TaskService taskService;
    private final MapperUi mapperUi;
    private final RepoService repoService;

    @RequestMapping(value = "new", method = RequestMethod.POST)
    @Transactional
    public boolean register(@RequestBody final NewRepoUI newRepoUI) {
        return successfulAdd(newRepoUI);
    }

    private boolean successfulAdd(final NewRepoUI newRepoUI) {
        return repoService.add(
                newRepoUI.getId(), newRepoUI.getName(), newRepoUI.getGitNick()
        ) != null;
    }

    /**
     * Return tasks in response depends on request parameters
     *
     * @param repoId                 repository id
     * @param maxTasksInResultReturn max amount of tasks which expected in response
     * @param onlySuccessful         if expected only tasks with successful passing status
     * @param onlyUnsuccessful       if expected only tasks with unsuccessful passing status
     */
    @RequestMapping(value = "{repoId}", method = RequestMethod.GET)
    public List<TaskUI> getTasks(@Min(value = 1, message = "Incorrect repoId value") @PathVariable(value = "repoId") final long repoId,
                                 @RequestParam(required = false, defaultValue = "300") final Integer maxTasksInResultReturn,
                                 @RequestParam(required = false) final boolean onlySuccessful,
                                 @RequestParam(required = false) final boolean onlyUnsuccessful) {
        LOGGER.debug("GET /repo/{} with maxTasksInResultReturn = {}, onlySuccessful = {}, onlyUnsuccessful = {}", repoId, maxTasksInResultReturn, onlySuccessful, onlyUnsuccessful);

        final User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return new ArrayList<>();
        }
        return mapperUi.mapTasks(taskService.findByUserIdRepoIdWithAppliedFilters(currentUser.getId(), repoId, maxTasksInResultReturn, onlySuccessful, onlyUnsuccessful));
    }
}
