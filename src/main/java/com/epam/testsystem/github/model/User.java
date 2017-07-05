package com.epam.testsystem.github.model;

import lombok.Builder;
import lombok.Data;

/**
 * github_test
 * Created on 05.07.17.
 */

@Data
@Builder
public class User {
    private long id;
    private String email;
    private String githubNick;
}
