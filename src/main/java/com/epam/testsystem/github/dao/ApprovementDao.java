package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.enums.ApprovementStatus;
import com.epam.testsystem.github.model.Approvement;
import com.epam.testsystem.github.model.ApprovementUserTask;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.epam.testsystem.github.dao.DaoExtractorUtil.AUT_ROW_MAPPER;
import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
@RequiredArgsConstructor
public class ApprovementDao {
    private final JdbcTemplate jdbcTemplate;

    public Approvement add(final long userId, final long taskId) {
        final LocalDateTime approveTime = LocalDateTime.now().withNano(0);
        jdbcTemplate.update(
                "INSERT IGNORE INTO approvements (task_id, user_id, approve_time) VALUES (?, ?, ?)",
                taskId, userId, approveTime);


        return Approvement.builder()
                .userId(userId)
                .taskId(taskId)
                .approve_time(approveTime)
                .mark(ApprovementStatus.VIEWED)
                .build();
    }

    public Optional<ApprovementUserTask> find(final long userId, final long taskId) {
        return Optional.ofNullable(
                singleResult(jdbcTemplate.query(
                        "SELECT * FROM (approvements JOIN users ON approvements.user_id = users.id) " +
                                "JOIN tasks ON approvements.task_id = tasks.id " +
                                "WHERE approvements.user_id = ? AND approvements.task_id = ?",
                        new Object[]{userId, taskId},
                        AUT_ROW_MAPPER
                        )
                )
        );
    }

    public List<ApprovementUserTask> find(final long taskId) {
        return jdbcTemplate.query(
                "SELECT * FROM (approvements JOIN users ON approvements.user_id = users.id) " +
                        "JOIN tasks ON approvements.task_id = tasks.id " +
                        "WHERE approvements.task_id = ?",
                new Object[]{taskId},
                AUT_ROW_MAPPER
        );
    }

    public Approvement update(final long userId,
                              final long taskId,
                              final ApprovementStatus mark,
                              final String comment) {
        final LocalDateTime approveTime = LocalDateTime.now().withNano(0);
        jdbcTemplate.update(
                "UPDATE approvements SET mark = ?, approve_time = ?, comment = ? " +
                        "WHERE user_id = ? AND task_id = ?",
                mark.name(), approveTime, comment, userId, taskId);

        return Approvement.builder()
                .userId(userId)
                .taskId(taskId)
                .approve_time(approveTime)
                .mark(mark)
                .comment(comment)
                .build();
    }
}
