package com.epam.testsystem.github.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * github_test
 * Created on 06.07.17.
 */

@Data
@Builder
public class UserWithTasks {
    private User user;
    private List<Task> tasks;
}
