package com.epam.testsystem.github.service.webhook;

import com.epam.testsystem.github.dao.UserDao;
import com.epam.testsystem.github.enums.EmailTemplateType;
import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.service.log.LogResolver;
import com.epam.testsystem.github.service.notification.mail.MailService;
import com.epam.testsystem.github.service.task.TaskService;
import com.epam.testsystem.github.util.MailInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Optional;

/**
 * github_test
 * Create on 7/13/2017.
 */

@Service
@RequiredArgsConstructor
public class GitlabParserService implements WebhookParserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebhookParserService.class);
    private static final SecureRandom RANDOM = new SecureRandom();
    private final LogResolver gitlabLogsResolver;
    private final TaskService taskService;
    private final UserDao userDao;
    private final MailService mailService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public boolean parse(String payload) {
        try {
            final JsonNode pullPayloadJson = objectMapper.readTree(payload);

            final String githubNick = pullPayloadJson.get("commit").get("author_name").asText();
            final String email = pullPayloadJson.get("commit").get("author_email").asText();

            final boolean status = pullPayloadJson.get("build_status").asText().equals("success");
            final long repoId = pullPayloadJson.get("project_id").asLong();
            final long buildId = pullPayloadJson.get("build_id").asLong();

            LOGGER.info("user {} with email {} has status {} in project request number {} with build id {}",
                    githubNick, email, status, repoId, buildId
            );

            LOGGER.info("try to get logs from gitlab");
            final String logs = gitlabLogsResolver.getLogs(githubNick, buildId);

            final Optional<User> userOptional = userDao.findByEmail(email);

            if (userOptional.isPresent()) {
                final long userId = userOptional.get().getId();
                final MailInfo mailInfo = MailInfo.builder().userName(userOptional.get().getGitNick()).build();
                taskService.addOrUpdate(userId, repoId, status, logs);
                mailService.sendMessage(email, "", "Github TestSystem",
                        EmailTemplateType.SOLUTION_RECEIVING_CONFIRMATION, mailInfo);
            } else {
                LOGGER.info("add new user {} with email {}", githubNick, email);
                final String password = generatePassword();
                final User user = userDao.add(email, githubNick, password);
                final MailInfo mailInfo = MailInfo.builder().userName(githubNick).password(password).build();
                taskService.addOrUpdate(user.getId(), repoId, status, logs);
                mailService.sendMessage(email, "", "Github TestSystem",
                        EmailTemplateType.SOLUTION_RECEIVING_CONFIRMATION_WITHOUT_REGISTRATION, mailInfo);
            }

            return true;
        } catch (final IOException e) {
            LOGGER.error("Can't parse json\nWith error {}", e.getMessage());
            return false;
        }
    }

    private String generatePassword() {
        return new BigInteger(43, RANDOM).toString(32);
    }

}
