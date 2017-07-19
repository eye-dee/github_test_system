package com.epam.testsystem.github.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * github_test
 * Created on 05.07.17.
 */

@Data
@Builder
public class Task {
    private long id;
    private long userId;
    private long repoId;
    private LocalDateTime registerTime;
    private TaskStatus status;
    private boolean successful;
    private String log;
}
