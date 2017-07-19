package com.epam.testsystem.github.service.notification.mail;

import com.epam.testsystem.github.enums.EmailTemplateType;
import com.epam.testsystem.github.exception.BusinessLogicException;
import com.epam.testsystem.github.util.MailInfo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:Daria_Makarova@epam.com">Daria Makarova</a>
 * @version 0.1
 * @since 0.1
 */
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfig;

    /**
     * Send an email message
     *
     * @param emailTo           email recipient
     * @param CC                email copy recipients
     * @param subject           a message topic
     * @param emailTemplateType {@link EmailTemplateType}
     * @param mailInfo          {@link MailInfo}
     */
    @Override
    public void sendMessage(final String emailTo, final String CC, final String subject, final EmailTemplateType emailTemplateType, final MailInfo mailInfo) {
        LOGGER.debug("=== Sending message === with emailTo={}, CC={}, subject={}, emailTemplateType={}, mailInfo={}",
                emailTo, CC, subject, emailTemplateType, mailInfo);

        validateDataForEmailSending(emailTo, subject, emailTemplateType);
        final Pattern emailPattern = Pattern.compile("^[\\w0-9+_.-]+@[\\w0-9.-]+\\.[a-zA-Z]{2,6}");
        if (!emailPattern.matcher(emailTo).matches()) {
            LOGGER.error("emailTo={} has incorrect format", emailTo);
            throw new BusinessLogicException("emailTo".concat(emailTo).concat(" has incorrect format"));
        }
        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            final Template emailTemplate = freemarkerConfig.getTemplate(emailTemplateType.getEmailTemplateByName());
            final String emailText = FreeMarkerTemplateUtils.processTemplateIntoString(emailTemplate, getEmailModelForTemplate(mailInfo, emailTemplateType));
            final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(emailTo);
            if (StringUtils.hasText(CC)) {
                helper.setCc(CC);
            }
            helper.setSubject(subject);
            helper.setText(emailText, true);

            mailSender.send(helper.getMimeMessage());
        } catch (Exception e) {
            LOGGER.error("No mail was sent because of : {}", e.getMessage());
            throw new BusinessLogicException("No mail was sent because of : ".concat(e.getMessage()));
        }
        LOGGER.debug("=== Message was sent ===");
    }

    private Map<String, String> getEmailModelForTemplate(MailInfo mailInfo, EmailTemplateType emailTemplateType) {
        if (Objects.isNull(mailInfo) || !StringUtils.hasText(mailInfo.getUserName())) {
            LOGGER.error("MailInfo or userName is null or empty. Message cannot be send");
            throw new BusinessLogicException("MailInfo or userName is null or empty. Message cannot be send");
        }
        final Map<String, String> model = new HashMap<>();
        model.put("userName", mailInfo.getUserName());
        switch (emailTemplateType) {
            case REGISTRATION_CONFIRMATION:
                if (!StringUtils.hasText(mailInfo.getEmail()) || !StringUtils.hasText(mailInfo.getPassword())) {
                    LOGGER.error("Email or password is null or empty. " +
                            "Message with template REGISTRATION_CONFIRMATION cannot be send");
                    throw new BusinessLogicException("Email or password is null or empty. " +
                            "Message with template REGISTRATION_CONFIRMATION cannot be send");
                }
                model.put("email", mailInfo.getEmail());
                model.put("password", mailInfo.getPassword());
                break;
            case SOLUTION_RECEIVING_CONFIRMATION_WITHOUT_REGISTRATION:
                if (!StringUtils.hasText(mailInfo.getPassword())) {
                    LOGGER.error("Password is null or empty. " +
                            "Message with template SOLUTION_RECEIVING_CONFIRMATION_WITHOUT_REGISTRATION cannot be send");

                    throw new BusinessLogicException("Password is null or empty. " +
                            "Message with template SOLUTION_RECEIVING_CONFIRMATION_WITHOUT_REGISTRATION cannot be send");
                }
                model.put("password", mailInfo.getPassword());
                break;
        }

        return model;
    }

    private void validateDataForEmailSending(String emailTo, String subject, EmailTemplateType emailTemplateType) {
        if (!StringUtils.hasText(emailTo) || !StringUtils.hasText(subject) || Objects.isNull(emailTemplateType)) {
            LOGGER.error("Destination email or email template or subject is null or empty. " +
                    "No mail with GitHub test tasks link was sent to email={}", emailTo);

            throw new BusinessLogicException(("Destination email or email template or subject is null or empty. . " +
                    "No mail with GitHub test tasks link was sent to email = ").concat(emailTo));
        }
    }
}
