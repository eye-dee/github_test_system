package com.epam.testsystem.github.service;

import com.epam.testsystem.github.exception.BusinessLogicException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.internet.MimeMessage;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

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

    private MailService mailService;
    private JavaMailSender javaMailSender;

    @Before
    public void setUp() {
        javaMailSender = mock(JavaMailSender.class);
        mailService = new MailServiceImpl(javaMailSender);
    }

    @Test(expected = BusinessLogicException.class)
    public void failedToSendEmailWithGitHubTasksLinkAndNoRecipient() {
        mailService.sendMessage("", null, "some test subject", "some test text");
    }

    @Test(expected = BusinessLogicException.class)
    public void failedToSendEmailWithGitHubTasksLinkAndNoSubject() {
        mailService.sendMessage(CORRECT_EMAIL_RECIPIENT, null, null, "some test text");
    }

    @Test(expected = BusinessLogicException.class)
    public void failedToSendEmailWithGitHubTasksLinkAndNoTextBody() {
        mailService.sendMessage(CORRECT_EMAIL_RECIPIENT, null, "some subject", "             ");
    }

    @Test
    public void sendMailWithoutException() {
        final MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        mailService.sendMessage(CORRECT_EMAIL_RECIPIENT, null, "some subject", "Please follow the instructions below : ");

        verify(javaMailSender, times(1)).send((MimeMessage) any());
    }

    @Test(expected = BusinessLogicException.class)
    public void sendMailWithIncorrectEmailFormat() {
        mailService.sendMessage(INCORRECT_EMAIL_RECIPIENT, null, "some subject", "Please follow the instructions below : ");
    }


}
