package com.epam.testsystem.github.web.controller;

import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.service.task.TaskService;
import com.epam.testsystem.github.service.user.UserService;
import com.epam.testsystem.github.web.mapper.MapperUi;
import com.epam.testsystem.github.web.model.TaskUI;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


    @RequestMapping(value = "{repoId}", method = RequestMethod.GET)
    public List<TaskUI> getTasks(@Min(value = 1, message = "Incorrect repoId value") @PathVariable(value = "repoId") final long repoId,
                                 @RequestParam(required = false, defaultValue = "300") Integer maxTasksInResultReturn,
                                 @RequestParam(required = false) boolean onlySuccessful,
                                 @RequestParam(required = false) boolean onlyUnsuccessful) {
        LOGGER.debug("GET /repo/{} with maxTasksInResultReturn = {}, onlySuccessful = {}, onlyUnsuccessful = {}", repoId, maxTasksInResultReturn, onlySuccessful, onlyUnsuccessful);

        final User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return new ArrayList<>();
        }
        return mapperUi.mapTasks(taskService.findByUserIdRepoIdWithAppliedFilters(currentUser.getId(), repoId, maxTasksInResultReturn, onlySuccessful, onlyUnsuccessful));
    }
}
