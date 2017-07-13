package com.epam.testsystem.github.service.mail;

import com.epam.testsystem.github.enums.EmailTemplateType;
import com.epam.testsystem.github.exception.BusinessLogicException;
import com.epam.testsystem.github.util.MailInfo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.SneakyThrows;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private Configuration freemarkerConfig;

    @Before
    public void setUp() {
        javaMailSender = mock(JavaMailSender.class);
        freemarkerConfig = mock(Configuration.class);
        mailService = new MailServiceImpl(javaMailSender, freemarkerConfig);
    }

    @Test(expected = BusinessLogicException.class)
    public void failedToSendEmailWithTasksRepositoryLinkAndNoRecipient() {
        final MailInfo mailInfo = MailInfo.builder().userName("userName").build();
        mailService.sendMessage("", null, "some test subject", EmailTemplateType.REPOSITORY_LINK, mailInfo);
    }

    @Test(expected = BusinessLogicException.class)
    public void failedToSendEmailWithTasksRepositoryLinkAndNoSubject() {
        final MailInfo mailInfo = MailInfo.builder().userName("userName").build();
        mailService.sendMessage(CORRECT_EMAIL_RECIPIENT, null, null, EmailTemplateType.REPOSITORY_LINK, mailInfo);
    }

    @Test(expected = BusinessLogicException.class)
    public void failedToSendEmailWithTasksRepositoryLinkAndNoEmailTemplate() {
        final MailInfo mailInfo = MailInfo.builder().userName("userName").build();
        mailService.sendMessage(CORRECT_EMAIL_RECIPIENT, null, "some subject", null, mailInfo);
    }

    @Test(expected = BusinessLogicException.class)
    public void failedToSendEmailWithTasksRepositoryLinkAndNoMailInfo() {
        mailService.sendMessage(CORRECT_EMAIL_RECIPIENT, null, "some subject", null, null);
    }

    @Test
    @SneakyThrows
    public void sendMailWithoutException() {
        final MimeMessage mimeMessage = mock(MimeMessage.class);
        final Template template = mock(Template.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(freemarkerConfig.getTemplate(EmailTemplateType.REPOSITORY_LINK.getEmailTemplateByName())).thenReturn(template);
        final MailInfo mailInfo = MailInfo.builder().userName("userName").build();

        mailService.sendMessage(CORRECT_EMAIL_RECIPIENT, null, "some subject", EmailTemplateType.REPOSITORY_LINK, mailInfo);

        verify(javaMailSender, times(1)).send((MimeMessage) any());
    }

    @Test(expected = BusinessLogicException.class)
    public void failedToSendMailWithIncorrectEmailFormat() {
        final MailInfo mailInfo = MailInfo.builder().userName("userName").email("email").password("password").build();

        mailService.sendMessage(INCORRECT_EMAIL_RECIPIENT, null, "some subject", EmailTemplateType.REPOSITORY_LINK, mailInfo);
    }

    @Test(expected = BusinessLogicException.class)
    public void failedToSendMailWithLackOfMailInfo() {
        final MailInfo mailInfo = MailInfo.builder().userName("userName").build();

        mailService.sendMessage(CORRECT_EMAIL_RECIPIENT, null, "some subject", EmailTemplateType.REGISTRATION_CONFIRMATION, mailInfo);
    }

}
