package com.epam.testsystem.github.service.task;

import com.epam.testsystem.github.dao.TaskDao;
import com.epam.testsystem.github.exception.BusinessLogicException;
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
    private static final int MAX_TASKS_AMOUNT_GETTING_FROM_DATABASE = 5_000;

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
        LOGGER.info("Set result for user {} and id {}", userId, id);
        return taskDao.setResultById(userId, id, successful, log);
    }

    @Override
    @Transactional
    public List<Task> findByUserIdRepoIdWithAppliedFilters(long userId, long repoId, int maxTasksInResultReturn, boolean onlySuccessful, boolean onlyUnsuccessful) {
        if (maxTasksInResultReturn <= 0 || maxTasksInResultReturn > MAX_TASKS_AMOUNT_GETTING_FROM_DATABASE) {
            LOGGER.error("maxTasksInResultReturn should be grater than 0 and less than {}", MAX_TASKS_AMOUNT_GETTING_FROM_DATABASE);
            throw new BusinessLogicException("maxTasksInResultReturn should be grater than 0 and less than " +
                    MAX_TASKS_AMOUNT_GETTING_FROM_DATABASE);
        }
        return (onlySuccessful && onlyUnsuccessful)
                ? taskDao.findByUserIdRepoIdWithAppliedFilters(userId, repoId, maxTasksInResultReturn, false, false)
                : taskDao.findByUserIdRepoIdWithAppliedFilters(userId, repoId, maxTasksInResultReturn, onlySuccessful, onlyUnsuccessful);
    }

    @Override
    public Task add(long userId, long repoId) {
        return taskDao.add(userId, repoId);
    }

    @Override
    public List<Task> findAllInProgress() {
        return taskDao.findAllInProgress();
    }
}
