package com.epam.testsystem.github.web.controller;

import com.epam.testsystem.github.enums.EmailTemplateType;
import com.epam.testsystem.github.enums.UserRoleType;
import com.epam.testsystem.github.service.notification.mail.MailService;
import com.epam.testsystem.github.service.user.UserService;
import com.epam.testsystem.github.util.MailInfo;
import com.epam.testsystem.github.web.model.NewUserUI;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    public boolean register(@RequestBody final NewUserUI newUserUI, @RequestParam(required = false, defaultValue ="ROLE_USER") UserRoleType roleType) {
        final boolean registration = successfulRegistration(newUserUI, roleType);
        MailInfo mailInfo = MailInfo.builder()
                .userName(newUserUI.getGitNick())
                .email(newUserUI.getEmail())
                .password(newUserUI.getPassword())
                .build();

        mailService.sendMessage(newUserUI.getEmail(), "", "Registration",
                EmailTemplateType.REGISTRATION_CONFIRMATION, mailInfo);

        return registration;
    }

    private boolean successfulRegistration(NewUserUI newUserUI, UserRoleType roleType) {
        return userService.register(
                newUserUI.getEmail(), newUserUI.getGitNick(), newUserUI.getPassword(), roleType
        ) != null;
    }
}
