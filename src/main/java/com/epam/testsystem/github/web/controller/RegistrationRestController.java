package com.epam.testsystem.github.web.controller;

import com.epam.testsystem.github.service.mail.MailService;
import com.epam.testsystem.github.service.user.UserService;
import com.epam.testsystem.github.web.model.NewUserUI;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * github_test
 * Create on 7/11/2017.
 */

@RestController
@RequestMapping(value = "registration")
@RequiredArgsConstructor
public class RegistrationRestController {
    private final UserService userService;
    private final MailService mailService;

    @RequestMapping(value = "user", method = RequestMethod.POST)
    @Transactional
    public boolean register(@RequestBody final NewUserUI newUserUI) {
        final boolean registration = successfulRegistration(newUserUI);

        mailService.sendMessage(newUserUI.getEmail(),"","Registration",
                "Your registration has done successful\n" +
                        "email = " + newUserUI.getEmail() + "\n" +
                        "password = " + newUserUI.getPassword() + "\n"
        );

        return registration;
    }

    private boolean successfulRegistration(NewUserUI newUserUI) {
        return userService.register(
                newUserUI.getEmail(), newUserUI.getGithubNick(), newUserUI.getPassword()
        ) != null;
    }
}
