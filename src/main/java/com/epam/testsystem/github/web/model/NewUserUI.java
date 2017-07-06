package com.epam.testsystem.github.web.model;

import lombok.Builder;
import lombok.Data;

/**
 * github_test
 * Created on 05.07.17.
 */

@Data
@Builder
public class NewUserUI {
    private String githubNick;
    private String email;
}
