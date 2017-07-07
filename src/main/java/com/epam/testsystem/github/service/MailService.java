package com.epam.testsystem.github.service;

import com.epam.testsystem.github.exception.BusinessLogicException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:Daria_Makarova@epam.com">Daria Makarova</a>
 * @version 0.1
 * @since 0.1
 */
@Service
public class MailService {
    private static final Logger Log = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSenderImpl mailSender;

    @Autowired
    public MailService(JavaMailSenderImpl mailSender) {
        this.mailSender = Objects.requireNonNull(mailSender);
    }

    /**
     * Send an email message with GitHub test tasks repository link
     *
     * @param emailTo     email recipient
     * @param CC          email copy recipients
     * @param subject     a message short description
     * @param messageText text with GitHub link to test tasks repository
     */
    public void sendGitHubTasksRepositoryLink(final String emailTo, final String CC, final String subject, final String messageText) {
        Log.debug("Async sendGitHubTasksRepositoryLink with emailTo={}, CC={}, subject={}, messageText={}", emailTo, CC, subject, messageText);
        if (StringUtils.isEmpty(emailTo) || !StringUtils.hasText(emailTo)
                || StringUtils.isEmpty(messageText) || !StringUtils.hasText(messageText)
                || StringUtils.isEmpty(subject) || !StringUtils.hasText(subject)) {
            Log.error("Destination email or message text or subject is null or empty. No mail with GitHub test tasks link was sent to email={}", emailTo);
            throw new BusinessLogicException("Destination email or message text or subject is null or empty. No mail with GitHub test tasks link was sent to email={}".concat(emailTo));
        }
        Pattern emailPattern = Pattern.compile("^[\\w0-9+_.-]+@[\\w0-9.-]+\\.[a-zA-Z]{2,6}");

        if (!emailPattern.matcher(emailTo).matches()) {
            Log.error("emailTo={} has incorrect format", emailTo);
            throw new BusinessLogicException("emailTo".concat(emailTo).concat(" has incorrect format"));

        }
        sendMessage(emailTo, subject, CC, messageText);
    }

    private void sendMessage(String emailTo, String subject, String CC, String text) {
        Log.debug("=== Sending message ===");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(emailTo);
            if (!StringUtils.isEmpty(CC) && StringUtils.hasText(CC)) {
                helper.setCc(CC);
            }
            helper.setSubject(subject);
            helper.setText(text);

            mailSender.send(helper.getMimeMessage());
        } catch (Exception e) {
            Log.error("No mail was sent because of : ", e.getMessage());
        }
        Log.debug("=== Message was sent ===");
    }


}
