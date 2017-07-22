package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.enums.ApprovementStatus;
import com.epam.testsystem.github.exception.BusinessLogicException;
import com.epam.testsystem.github.model.Approvement;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.epam.testsystem.github.dao.DaoExtractorUtil.APPROVEMENT_ROW_MAPPER;
import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
@RequiredArgsConstructor
public class ApprovementDao {
    private final JdbcTemplate jdbcTemplate;

    public Approvement add(final long taskId, final long userId) {
        final LocalDateTime approveTime = LocalDateTime.now().withNano(0);
        jdbcTemplate.update(
                "INSERT INTO approvements (task_id, user_id, approve_time) " +
                        "VALUES (?, ?, ?)",
                taskId, userId, approveTime);

        return find(taskId, userId).orElseThrow(() -> new BusinessLogicException("Error in approvement add"));
    }

    public Optional<Approvement> find(final long taskId, final long userId) {
        return Optional.ofNullable(
                singleResult(jdbcTemplate.query(
                        "SELECT * FROM approvements WHERE user_id = ? AND task_id = ?",
                        new Object[]{userId, taskId},
                        APPROVEMENT_ROW_MAPPER)
                )
        );
    }

    public Approvement update(final long taskId, final long userId, final ApprovementStatus mark) {
        final LocalDateTime approveTime = LocalDateTime.now().withNano(0);
        jdbcTemplate.update(
                "UPDATE approvements SET mark = ?, approve_time = ?",
                mark.name(), approveTime);

        return find(taskId, userId).orElseThrow(() -> new BusinessLogicException("Error in approvement update"));
    }
}
