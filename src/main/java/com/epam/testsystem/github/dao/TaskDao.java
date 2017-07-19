package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.TaskStatus;
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
                            final long repoId,
                            final boolean successful,
                            final String log) {
        final LocalDateTime registerTime = LocalDateTime.now().withNano(0);
        jdbcTemplate.update(
                "INSERT INTO tasks(user_id, repo_id, register_time, successful, log) " +
                        "VALUES(?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "id = LAST_INSERT_ID(id), successful = ?, log = ?, status = 'CHECKED'",
                userId, repoId, registerTime, successful, log,
                successful, log);

        return jdbcTemplate.queryForObject("SELECT * FROM tasks where id = LAST_INSERT_ID()", TASK_ROW_MAPPER);
    }

    public Task add(long userId, long repoId) {
        final LocalDateTime registerTime = LocalDateTime.now().withNano(0);
        jdbcTemplate.update(
                "INSERT INTO tasks(user_id, repo_id, register_time, log) " +
                        "VALUES(?, ?, ?, ?)",
                userId, repoId, registerTime, "{}");

        final Integer id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

        return Task.builder()
                .id(id)
                .userId(userId)
                .registerTime(registerTime)
                .successful(false)
                .status(TaskStatus.PROGRESS)
                .log("{}")
                .repoId(repoId)
                .build();
    }

    public List<Task> findAllByUserId(final long userId) {
        return jdbcTemplate.query(
                "SELECT * FROM tasks WHERE user_id = ?",
                new Object[]{userId},
                TASK_ROW_MAPPER
        );
    }

    public List<Task> findAllByUserId(final long userId, final String cycleName) {
        return jdbcTemplate.query(
                "SELECT id, user_id, repo_id, register_time, status, successful, log->'$." +
                        cycleName +
                        "' AS 'tasks.log'" +
                        " FROM tasks WHERE user_id = ?",
                new Object[]{userId},
                TASK_ROW_MAPPER
        );
    }

    public List<Task> findByUserIdRepoIdWithAppliedFilters(final long userId, final long repoId,
                                                           final Integer maxTasksInResultReturn, final boolean onlySuccessful,
                                                           final boolean onlyUnsuccessful) {
        StringBuilder query = new StringBuilder("SELECT * FROM tasks WHERE user_id = ? AND repo_id = ?");
        if (onlySuccessful) {
            query.append(" AND successful = true");
        }
        if (onlyUnsuccessful) {
            query.append(" AND successful = false");
        }
        query.append(" ORDER BY register_time DESC LIMIT ?");
        return jdbcTemplate.query(
                query.toString(),
                new Object[]{userId, repoId, maxTasksInResultReturn},
                TASK_ROW_MAPPER);
    }

    public boolean setResultById(final long userId,
                                 final long id,
                                 final boolean successful,
                                 final String log) {
        return jdbcTemplate.update(
                "UPDATE tasks SET status = ?, successful = ?, log = ? WHERE user_id = ? AND id = ?",
                TaskStatus.CHECKED.name(), successful, log, userId, id
        ) > 0;
    }

    public List<Task> findAllInProgress() {
        return jdbcTemplate.query(
                "SELECT * FROM tasks WHERE status = ?",
                new Object[]{TaskStatus.PROGRESS.name()},
                TASK_ROW_MAPPER
        );
    }
}
