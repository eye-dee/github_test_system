package com.epam.testsystem.github.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * github_test
 * Created on 06.07.17.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskUI {
    private boolean successful;
    private String startTime;
}
