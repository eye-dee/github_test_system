package com.epam.testsystem.github.web;

import com.epam.testsystem.github.dao.UserDao;
import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.web.model.NewUserUI;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * github_test
 * Created on 05.07.17.
 */

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserRestController {
    private final UserDao userDao;

    @RequestMapping("register")
    public boolean register(
            @RequestBody NewUserUI newUserUI
    ) {
        userDao.add(newUserUI.getEmail(), newUserUI.getGithubNick());
        return true;
    }
}
