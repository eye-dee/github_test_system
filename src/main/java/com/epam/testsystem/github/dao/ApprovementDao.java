package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.enums.ApprovementStatus;
import com.epam.testsystem.github.model.Approvement;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.epam.testsystem.github.dao.DaoExtractorUtil.APPROVEMENT_ROW_MAPPER;
import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
@RequiredArgsConstructor
public class ApprovementDao {
    private final JdbcTemplate jdbcTemplate;

    public Approvement add(final long userId, final long taskId) {
        final LocalDateTime approveTime = LocalDateTime.now();
        jdbcTemplate.update(
                "INSERT INTO approvements (task_id, user_id, approve_time) VALUES (?, ?, ?)" +
                        "ON DUPLICATE KEY UPDATE id = last_insert_id(id)",
                taskId, userId, approveTime);

        return singleResult(jdbcTemplate.query(
                "SELECT * FROM approvements WHERE id = last_insert_id()",
                APPROVEMENT_ROW_MAPPER)
        );
    }

    public Optional<Approvement> find(final long userId, final long taskId) {
        return Optional.ofNullable(
                singleResult(jdbcTemplate.query(
                        "SELECT * FROM approvements WHERE user_id = ? AND task_id = ?",
                        new Object[]{userId, taskId},
                        APPROVEMENT_ROW_MAPPER)
                )
        );
    }

    public List<Approvement> find(final long taskId) {
        return jdbcTemplate.query(
                "SELECT * FROM approvements WHERE task_id = ?",
                new Object[]{taskId},
                APPROVEMENT_ROW_MAPPER
        );
    }

    public Approvement update(final long userId,
                              final long taskId,
                              final ApprovementStatus mark,
                              final String comment) {
        final LocalDateTime approveTime = LocalDateTime.now();
        jdbcTemplate.update(
                "UPDATE approvements SET id = last_insert_id(id), mark = ?, approve_time = ?, comment = ? " +
                        "WHERE user_id = ? AND task_id = ?",
                mark.name(), approveTime, comment, userId, taskId);

        return singleResult(jdbcTemplate.query(
                "SELECT * FROM approvements WHERE id = last_insert_id()",
                APPROVEMENT_ROW_MAPPER)
        );
    }
}
