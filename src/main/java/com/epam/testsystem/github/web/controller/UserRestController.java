package com.epam.testsystem.github.web.controller;

import com.epam.testsystem.github.dao.UserDao;
import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.service.user.UserService;
import com.epam.testsystem.github.web.mapper.UserMapper;
import com.epam.testsystem.github.web.model.UserUI;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * github_test
 * Created on 05.07.17.
 */

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserRestController {
    private final UserDao userDao;
    private final UserService userService;
    private final UserMapper userMapper;

    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public List<UserUI> getAll() {
        return userMapper.mapUsers(userDao.findAllWithTasks());
    }

    @RequestMapping(value = "tasks", method = RequestMethod.GET)
    public UserUI currentUser() {
        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            throw new RuntimeException("No user login");
        } else {
            return userMapper.mapUser(userDao.findAllWithTasks().get(0));
        }
    }
}
