package com.epam.testsystem.github.service.travis;

import com.epam.testsystem.github.dao.TaskDao;
import com.epam.testsystem.github.dao.UserDao;
import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.service.logs.TravisLogsResolver;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

/**
 * github_test
 * Create on 7/10/2017.
 */

@Service
@RequiredArgsConstructor
public class TravisParserServiceImpl implements TravisParserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TravisParserService.class);
    private final TravisLogsResolver travisLogsResolver;
    private final TaskDao taskDao;
    private final UserDao userDao;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public boolean parse(String payload) {
        try {
            final JsonNode pullPayloadJson = objectMapper.readTree(payload);

            final String githubNick = pullPayloadJson.get("author_name").asText();
            final String email = pullPayloadJson.get("author_email").asText();

            final boolean status = pullPayloadJson.get("status").asInt() == 0;
            final long pullId = pullPayloadJson.get("pull_request_number").asLong();
            final long buildId = pullPayloadJson.get("id").asLong();

            LOGGER.info("user {} with email {} has status {} in pull request number {} with build id {}",
                    githubNick, email, status, pullId, buildId
            );

            LOGGER.info("try to get logs from travis");
            final String logs = travisLogsResolver.getLogs(buildId);

            final Optional<User> userOptional = userDao.findByEmail(email);

            if (userOptional.isPresent()) {
                final long userId = userOptional.get().getId();
                taskDao.addOrUpdate(userId, pullId, status, logs);
            } else {
                LOGGER.info("add new user {} with email {}", githubNick, email);
                final User user = userDao.add(email, githubNick);
                taskDao.addOrUpdate(user.getId(), pullId, status, logs);
            }

            return true;
        } catch (final IOException e) {
            LOGGER.error("Can't parse json\nWith error {}", e.getMessage());
            return false;
        }
    }
}
