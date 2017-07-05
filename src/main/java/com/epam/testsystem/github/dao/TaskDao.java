package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * github_test
 * Created on 05.07.17.
 */

@Repository
@Transactional(propagation = Propagation.MANDATORY)
@RequiredArgsConstructor
public class TaskDao {
    private final JdbcTemplate jdbcTemplate;

    public Task add(final long userId) {
        return null;
    }

    public List<Task> findAllInProgress() {
        return null;
    }

    public Task changeStatusById(final long id, final TaskStatus newStatus) {
        return null;
    }

    public Task setResultById(final long id, final boolean successful) {
        return null;
    }
}
