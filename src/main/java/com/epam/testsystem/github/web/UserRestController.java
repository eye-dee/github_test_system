package com.epam.testsystem.github.web;

import com.epam.testsystem.github.dao.UserDao;
import com.epam.testsystem.github.web.model.NewUserUI;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @Transactional
    public boolean register(
            @RequestBody NewUserUI newUserUI
    ) {
        userDao.add(newUserUI.getEmail(), newUserUI.getGithubNick());
        return true;
    }
}
