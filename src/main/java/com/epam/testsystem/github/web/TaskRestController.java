package com.epam.testsystem.github.web;

import com.epam.testsystem.github.dao.TaskDao;
import com.epam.testsystem.github.dao.UserDao;
import com.epam.testsystem.github.model.User;
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

    private final UserDao userDao;
    private final TaskDao taskDao;

    @RequestMapping(value = "new", method = RequestMethod.POST)
    @Transactional
    public boolean addNewTask(@RequestBody final NewTaskUI newTaskUI) {
        final Optional<User> optionalUser = userDao.findByEmail(newTaskUI.getEmail());
        if (optionalUser.isPresent()) {
            taskDao.add(optionalUser.get().getId(), 0);
            // TODO: 06.07.17 send message to mail
            return true;
        } else {
            return false;
        }
    }
}
