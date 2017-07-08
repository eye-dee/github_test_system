package com.epam.testsystem.github.web;

import com.epam.testsystem.github.dao.TaskDao;
import com.epam.testsystem.github.dao.UserDao;
import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.web.model.NewPullPayload;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

/**
 * github_test
 * Created on 08.07.17.
 */

@RestController
@RequestMapping("travisci")
@RequiredArgsConstructor
public class TravisRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TravisRestController.class);

    private final UserDao userDao;
    private final TaskDao taskDao;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public void newPull(@RequestBody final NewPullPayload newPullPayload) {
        try {
            final JsonNode pullPayloadJson = objectMapper.readTree(newPullPayload.getPayload());

            final String githubNick = pullPayloadJson.get("author_name").asText();
            final String email = pullPayloadJson.get("author_email").asText();

            final boolean status = pullPayloadJson.get("status").asInt() == 0;

            final Optional<User> userOptional = userDao.findByEmail(email);

            if (userOptional.isPresent()) {
                final long userId = userOptional.get().getId();
                final Task add = taskDao.add(userId);
                taskDao.setResultById(userId,add.getId(),status);
            } else {
                final User user = userDao.add(email, githubNick);
                final Task task = taskDao.add(user.getId());
                taskDao.setResultById(user.getId(),task.getId(),status);
            }

        } catch (final IOException e) {
            LOGGER.error("Can't parse json\nWith error {}", e.getMessage());
        }
    }
}
