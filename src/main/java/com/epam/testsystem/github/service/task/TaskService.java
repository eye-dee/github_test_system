package com.epam.testsystem.github.service.task;

import com.epam.testsystem.github.model.Task;

import java.util.List;

/**
 * github_test
 * Create on 7/13/2017.
 */
public interface TaskService {
    Task addOrUpdate(final long userId, final long repoId, final boolean successful, final String log);

    List<Task> findAllByUserId(final long userId);

    boolean setResultById(final long userId, final long id, final boolean successful, final String log);
}
