package com.epam.testsystem.github.web.controller;

import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.service.task.TaskService;
import com.epam.testsystem.github.service.user.UserService;
import com.epam.testsystem.github.web.model.NewTaskUI;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * github_test
 * Created on 06.07.17.
 */

@RestController
@RequestMapping("task")
@RequiredArgsConstructor
public class TaskRestController {

    private final UserService userService;
    private final TaskService taskService;

    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public boolean addNewTask(@RequestBody final NewTaskUI newTaskUI) {
        final Optional<User> optionalUser = userService.findByEmail(newTaskUI.getEmail());
        if (optionalUser.isPresent()) {
            taskService.add(optionalUser.get().getId(), newTaskUI.getRepoId());
            // TODO: 06.07.17 send message to mail
            return true;
        } else {
            return false;
        }
    }
}
