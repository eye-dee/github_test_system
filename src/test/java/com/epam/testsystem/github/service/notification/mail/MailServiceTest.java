package com.epam.testsystem.github.service.notification.mail;

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

import java.io.IOException;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private static final String FAKE_CC = "fklfkvns@asdsdfs.com";
    private static final String PASSWORD = "12345";
    private static final String INCORRECT_EMAIL_RECIPIENT = "hgchshgchs.com";
    private static final String USER_NAME = "USER_NAME";

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
        final MailInfo mailInfo = MailInfo.builder().userName(USER_NAME).build();
        mailService.sendMessage("", null, "some test subject", EmailTemplateType.REPOSITORY_LINK, mailInfo);
    }

    @Test(expected = BusinessLogicException.class)
    public void failedToSendEmailWithTasksRepositoryLinkAndNoSubject() {
        final MailInfo mailInfo = MailInfo.builder().userName(USER_NAME).build();
        mailService.sendMessage(CORRECT_EMAIL_RECIPIENT, null, null, EmailTemplateType.REPOSITORY_LINK, mailInfo);
    }

    @Test(expected = BusinessLogicException.class)
    public void failedToSendEmailWithTasksRepositoryLinkAndNoRecipientWithFakeCC() {
        final MailInfo mailInfo = MailInfo.builder().userName(USER_NAME).build();
        mailService.sendMessage("", FAKE_CC, "some test subject", EmailTemplateType.REPOSITORY_LINK, mailInfo);
    }

    @Test(expected = BusinessLogicException.class)
    public void failedToSendEmailWithTasksRepositoryLinkAndNoSubjectWithFakeCC() {
        final MailInfo mailInfo = MailInfo.builder().userName(USER_NAME).build();
        mailService.sendMessage(CORRECT_EMAIL_RECIPIENT, FAKE_CC, null, EmailTemplateType.REPOSITORY_LINK, mailInfo);
    }

    @Test(expected = BusinessLogicException.class)
    public void failedToSendEmailWithTasksRepositoryLinkAndNoEmailTemplate() {
        final MailInfo mailInfo = MailInfo.builder().userName(USER_NAME).build();
        mailService.sendMessage(CORRECT_EMAIL_RECIPIENT, null, "some subject", null, mailInfo);
    }

    @Test(expected = BusinessLogicException.class)
    public void failedToSendEmailWithTasksRepositoryLinkWithNullEmailTemplate() {
        mailService.sendMessage(CORRECT_EMAIL_RECIPIENT, null, "some subject", null, null);
    }

    @Test(expected = BusinessLogicException.class)
    public void failedToSendEmailWithTasksRepositoryLinkWithNullMailInfo() {
        mailService.sendMessage(CORRECT_EMAIL_RECIPIENT, null, "some subject", EmailTemplateType.REPOSITORY_LINK, null);
    }

    @Test
    @SneakyThrows
    public void testModelPuttingInRegistrationConfirmation() {
        final MailInfo mailInfo = mock(MailInfo.class);
        doReturn(CORRECT_EMAIL_RECIPIENT).when(mailInfo).getEmail();
        doReturn(PASSWORD).when(mailInfo).getPassword();
        doReturn(USER_NAME).when(mailInfo).getUserName();

        final MimeMessage mimeMessage = mock(MimeMessage.class);
        final Template template = mock(Template.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(freemarkerConfig.getTemplate(EmailTemplateType.REGISTRATION_CONFIRMATION.getEmailTemplateByName())).thenReturn(template);

        mailService.sendMessage(CORRECT_EMAIL_RECIPIENT, "", "some subject", EmailTemplateType.REGISTRATION_CONFIRMATION, mailInfo);

        verify(mailInfo, times(2)).getPassword();
        verify(mailInfo, times(2)).getEmail();
    }

    @Test()
    public void testModelPuttingInRegistrationConfirmationNullPasswordException() throws IOException {
        final MailInfo mailInfo = mock(MailInfo.class);
        doReturn(CORRECT_EMAIL_RECIPIENT).when(mailInfo).getEmail();
        doReturn("").when(mailInfo).getPassword();
        doReturn(USER_NAME).when(mailInfo).getUserName();

        final MimeMessage mimeMessage = mock(MimeMessage.class);
        final Template template = mock(Template.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        final EmailTemplateType emailTemplateType = EmailTemplateType.SOLUTION_RECEIVING_CONFIRMATION_WITHOUT_REGISTRATION;

        when(freemarkerConfig.getTemplate(emailTemplateType.getEmailTemplateByName())).thenReturn(template);

        assertThatThrownBy(
                () -> mailService.sendMessage(
                        CORRECT_EMAIL_RECIPIENT, "", "some subject", emailTemplateType, mailInfo
                )
        )
                .isInstanceOf(BusinessLogicException.class)
                .hasMessageMatching(".*Password is null or empty.*");
    }

    @Test
    @SneakyThrows
    public void testModelPuttingInSolutionReceivingWithoutRegistration() {
        final MailInfo mailInfo = mock(MailInfo.class);
        doReturn(PASSWORD).when(mailInfo).getPassword();
        doReturn(USER_NAME).when(mailInfo).getUserName();

        final MimeMessage mimeMessage = mock(MimeMessage.class);
        final Template template = mock(Template.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        final EmailTemplateType emailTemplateType = EmailTemplateType.SOLUTION_RECEIVING_CONFIRMATION_WITHOUT_REGISTRATION;
        when(freemarkerConfig.getTemplate(emailTemplateType.getEmailTemplateByName())).thenReturn(template);

        mailService.sendMessage(CORRECT_EMAIL_RECIPIENT, "", "some subject", emailTemplateType, mailInfo);

        verify(mailInfo, times(2)).getPassword();
    }

    @Test
    @SneakyThrows
    public void sendMailWithoutException() {
        final MimeMessage mimeMessage = mock(MimeMessage.class);
        final Template template = mock(Template.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(freemarkerConfig.getTemplate(EmailTemplateType.REPOSITORY_LINK.getEmailTemplateByName())).thenReturn(template);
        final MailInfo mailInfo = MailInfo.builder().userName(USER_NAME).build();

        mailService.sendMessage(CORRECT_EMAIL_RECIPIENT, null, "some subject", EmailTemplateType.REPOSITORY_LINK, mailInfo);

        verify(javaMailSender, times(1)).send((MimeMessage) any());
    }

    @Test(expected = BusinessLogicException.class)
    public void failedToSendMailWithIncorrectEmailFormat() {
        final MailInfo mailInfo = MailInfo.builder().userName(USER_NAME).email("email").password("password").build();

        mailService.sendMessage(INCORRECT_EMAIL_RECIPIENT, null, "some subject", EmailTemplateType.REPOSITORY_LINK, mailInfo);
    }

    @Test(expected = BusinessLogicException.class)
    public void failedToSendMailWithLackOfMailInfo() {
        final MailInfo mailInfo = MailInfo.builder().userName(USER_NAME).build();

        mailService.sendMessage(CORRECT_EMAIL_RECIPIENT, null, "some subject", EmailTemplateType.REGISTRATION_CONFIRMATION, mailInfo);
    }

}
