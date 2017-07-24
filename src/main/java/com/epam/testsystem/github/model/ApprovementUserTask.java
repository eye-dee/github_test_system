package com.epam.testsystem.github.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApprovementUserTask {
    private Task task;
    private User operator;
    private Approvement approvement;
}
