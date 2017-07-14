package com.epam.testsystem.github.web.controller;

import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.service.task.TaskService;
import com.epam.testsystem.github.service.user.UserService;
import com.epam.testsystem.github.web.mapper.MapperUi;
import com.epam.testsystem.github.web.model.TaskUI;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * github_test
 * Create on 7/13/2017.
 */

@RestController
@RequestMapping("repo")
@RequiredArgsConstructor
public class RepoRestController {
    private final UserService userService;
    private final TaskService taskService;
    private final MapperUi mapperUi;

    @RequestMapping(value = "{repoId}", method = RequestMethod.GET)
    public List<TaskUI> getTasks(@PathVariable(value = "repoId") final long repoId) {
        final User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return new ArrayList<>();
        }
        return mapperUi.mapTasks(taskService.findAllByUserIdRepoId(currentUser.getId(), repoId));
    }
}
