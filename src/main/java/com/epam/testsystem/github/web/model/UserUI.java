package com.epam.testsystem.github.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * github_test
 * Created on 06.07.17.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUI {
    private String email;
    private String gitNick;
    private List<TaskUI> tasks;
}
