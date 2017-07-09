package com.epam.testsystem.github.service;

import com.epam.testsystem.github.exception.BusinessLogicException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;

/**
 * @author <a href="mailto:Daria_Makarova@epam.com">Daria Makarova</a>
 * @version 0.1
 * @since 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
public class MailServiceTest {
    private static final String CORRECT_EMAIL_RECIPIENT = "hghc4sa43hz@jbnjds.com";
    private static final String INCORRECT_EMAIL_RECIPIENT = "hgchshgchs.com";

    @Autowired
    private MailService mailServiceImpl;

    @Test(expected = BusinessLogicException.class)
    public void failedToSendEmailWithGitHubTasksLinkAndNoRecipient() {
        mailServiceImpl.sendGitHubTasksRepositoryLink("", null, "some test subject", "some test text");
    }

    @Test(expected = BusinessLogicException.class)
    public void failedToSendEmailWithGitHubTasksLinkAndNoSubject() {
        mailServiceImpl.sendGitHubTasksRepositoryLink(CORRECT_EMAIL_RECIPIENT, null, null, "some test text");
    }

    @Test(expected = BusinessLogicException.class)
    public void failedToSendEmailWithGitHubTasksLinkAndNoTextBody() {
        mailServiceImpl.sendGitHubTasksRepositoryLink(CORRECT_EMAIL_RECIPIENT, null, "some subject", "             ");
    }

    //TODO Maybe add test method without actually sending message to the fake email address
    @Test
    public void sendMailWithoutException() {
        mailServiceImpl.sendGitHubTasksRepositoryLink(CORRECT_EMAIL_RECIPIENT, null, "some subject", "Please follow the instructions below : ");
    }

    @Test(expected = BusinessLogicException.class)
    public void sendMailWithIncorrectEmailFormat() {
        mailServiceImpl.sendGitHubTasksRepositoryLink(INCORRECT_EMAIL_RECIPIENT, null, "some subject", "Please follow the instructions below : ");
    }


}
