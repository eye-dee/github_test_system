package com.epam.testsystem.github.service.approvement;

import com.epam.testsystem.github.model.Approvement;

import java.util.List;
import java.util.Optional;

public interface ApprovementService {
    Approvement add(long userId, long taskId);

    Optional<Approvement> find(long userId, long taskId);

    boolean markGood(long userId, long taskId);

    boolean markBad(long userId, long taskId);

    List<Approvement> find(long taskId);
}
