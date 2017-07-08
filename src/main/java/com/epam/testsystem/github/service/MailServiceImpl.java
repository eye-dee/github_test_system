package com.epam.testsystem.github.service;

import com.epam.testsystem.github.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:Daria_Makarova@epam.com">Daria Makarova</a>
 * @version 0.1
 * @since 0.1
 */
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;

    /**
     * Send an email message with GitHub test tasks repository link
     *
     * @param emailTo     email recipient
     * @param CC          email copy recipients
     * @param subject     a message topic
     * @param messageText text with GitHub link to test tasks repository
     */
    public void sendMessage(final String emailTo, final String CC,
                                              final String subject, final String messageText) {
        LOGGER.debug("Async sendGitHubTasksRepositoryLink with emailTo={}, CC={}, subject={}, messageText={}",
                emailTo, CC, subject, messageText);

        validateDataForEmailSending(emailTo, subject, messageText);
        final Pattern emailPattern = Pattern.compile("^[\\w0-9+_.-]+@[\\w0-9.-]+\\.[a-zA-Z]{2,6}");

        if (!emailPattern.matcher(emailTo).matches()) {
            LOGGER.error("emailTo={} has incorrect format", emailTo);
            throw new BusinessLogicException("emailTo".concat(emailTo).concat(" has incorrect format"));
        }
        send(emailTo, subject, CC, messageText);
    }

    private void validateDataForEmailSending(final String emailTo, final String subject, final String messageText) {
        if (!StringUtils.hasText(emailTo) || !StringUtils.hasText(messageText) || !StringUtils.hasText(subject)) {
            LOGGER.error("Destination email or message text or subject is null or empty. No mail with GitHub test tasks link was sent to email={}", emailTo);
            throw new BusinessLogicException("Destination email or message text or subject is null or empty. No mail with GitHub test tasks link was sent to email={}".concat(emailTo));
        }
    }

    private void send(final String emailTo, final String subject, final String CC, final String text) {
        LOGGER.debug("=== Sending message ===");
        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(emailTo);
            if (StringUtils.hasText(CC)) {
                helper.setCc(CC);
            }
            helper.setSubject(subject);
            helper.setText(text);

            mailSender.send(helper.getMimeMessage());
        } catch (final Exception e) {
            LOGGER.error("No mail was sent because of : ", e.getMessage());
        }
        LOGGER.debug("=== Message was sent ===");
    }

}
