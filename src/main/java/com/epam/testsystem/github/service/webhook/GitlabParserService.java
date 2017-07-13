package com.epam.testsystem.github.service.webhook;

import com.epam.testsystem.github.dao.TaskDao;
import com.epam.testsystem.github.dao.UserDao;
import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.service.logs.LogResolver;
import com.epam.testsystem.github.service.mail.MailService;
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
    private final TaskDao taskDao;
    private final UserDao userDao;
    private final MailService mailService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public boolean parse(String payload) {
        try {
            final JsonNode pullPayloadJson = objectMapper.readTree(payload);

            final String githubNick = pullPayloadJson.get("user").get("name").asText();
            final String email = pullPayloadJson.get("user").get("email").asText();

            final boolean status = pullPayloadJson.get("status").asInt() == 0;
            final long repoId = pullPayloadJson.get("project_id").asLong();
            final long buildId = pullPayloadJson.get("build_id").asLong();

            LOGGER.info("user {} with email {} has status {} in project request number {} with build id {}",
                    githubNick, email, status, repoId, buildId
            );

            LOGGER.info("try to get logs from travis");
            final String logs = gitlabLogsResolver.getLogs(buildId);

            final Optional<User> userOptional = userDao.findByEmail(email);

            if (userOptional.isPresent()) {
                final long userId = userOptional.get().getId();
                taskDao.addOrUpdate(userId, repoId, status, logs);
                mailService.sendMessage(email,"", "Github TestSystem",
                        "We are received your solution");
            } else {
                LOGGER.info("add new user {} with email {}", githubNick, email);
                final String password = generatePassword();
                final User user = userDao.add(email, githubNick, password);
                taskDao.addOrUpdate(user.getId(), repoId, status, logs);
                mailService.sendMessage(email,"", "Github TestSystem",
                        "You are not register yet on our system, but we get your solution" +
                                " You can access by password " + password);
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
