package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.epam.testsystem.github.dao.DaoExtractorUtil.TASK_ROW_MAPPER;

/**
 * github_test
 * Created on 05.07.17.
 */

@Repository
@Transactional(propagation = Propagation.MANDATORY)
@RequiredArgsConstructor
public class TaskDao {
    private final JdbcTemplate jdbcTemplate;

    public Task addOrUpdate(final long userId,
                            final long pullId,
                            final boolean successful,
                            final String log) {
        final LocalDateTime registerTime = LocalDateTime.now().withNano(0);
        jdbcTemplate.update(
                "INSERT INTO tasks(user_id, register_time, pull_id, successful, log) " +
                        "VALUES(?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "id = LAST_INSERT_ID(id), successful = ?, log = ?",
                userId, registerTime, pullId, successful, log,
                successful, log);

        final Integer id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

        return Task.builder()
                .id(id)
                .userId(userId)
                .registerTime(registerTime)
                .successful(false)
                .log(log)
                .pullId(pullId)
                .build();
    }

    public List<Task> findAllByUserId(final long userId) {
        return jdbcTemplate.query(
                "SELECT * FROM tasks WHERE user_id = ?",
                new Object[]{userId},
                TASK_ROW_MAPPER
        );
    }

    public boolean setResultById(final long userId,
                                 final long id,
                                 final boolean successful,
                                 final String log) {
        return jdbcTemplate.update(
                "UPDATE tasks SET successful = ?, log = ? WHERE user_id = ? AND id = ?",
                successful, log, userId, id
        ) > 0;
    }
}
