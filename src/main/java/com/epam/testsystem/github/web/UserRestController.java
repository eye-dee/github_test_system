package com.epam.testsystem.github.web;

import com.epam.testsystem.github.dao.UserDao;
import com.epam.testsystem.github.web.mapper.UserMapper;
import com.epam.testsystem.github.web.model.NewUserUI;
import com.epam.testsystem.github.web.model.UserUI;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final UserMapper userMapper;

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public boolean register(@RequestBody NewUserUI newUserUI) {
        userDao.add(newUserUI.getEmail(), newUserUI.getGithubNick());
        return true;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<UserUI> getAll() {
        return userMapper.mapUsers(userDao.findAllWithTasks());
    }
}
