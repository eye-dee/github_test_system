package com.epam.testsystem.github.service.task;

import com.epam.testsystem.github.dao.TaskDao;
import com.epam.testsystem.github.model.GradleLog;
import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.service.log.LogParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * github_test
 * Create on 7/13/2017.
 */

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final TaskDao taskDao;
    private final LogParser logParser;

    @Override
    @Transactional
    public Task addOrUpdate(long userId, long repoId, boolean successful, String log) {
        LOGGER.info("add or update new task for user {} repo {}", userId, repoId);

        final GradleLog structedLog = logParser.getStructedLog(log);
        try {
            final String parsedLogs = OBJECT_MAPPER.writeValueAsString(structedLog);

            return taskDao.addOrUpdate(userId, repoId, successful, parsedLogs);
        } catch (JsonProcessingException e) {
            LOGGER.warn("can't convert parsed logs. Will user standart\nError = {}", e.getMessage());
            return taskDao.addOrUpdate(userId, repoId, successful, log);
        }

    }

    @Override
    @Transactional
    public List<Task> findAllByUserId(long userId) {
        return taskDao.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public boolean setResultById(long userId, long id, boolean successful, String log) {
        return taskDao.setResultById(userId, id, successful, log);
    }
}
