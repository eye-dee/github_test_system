package com.epam.testsystem.github.web.model;

import lombok.Builder;
import lombok.Data;

/**
 * github_test
 * Created on 06.07.17.
 */

@Data
@Builder
public class NewTaskUI {
    private String email;
    private String githubNick;
}
