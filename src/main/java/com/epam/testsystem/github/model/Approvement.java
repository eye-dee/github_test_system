package com.epam.testsystem.github.model;

import com.epam.testsystem.github.enums.ApprovementStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Approvement {
    private long taskId;
    private long userId;
    private ApprovementStatus mark;
    private LocalDateTime approve_time;
    private String comment;
}
