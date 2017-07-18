package com.epam.testsystem.github.model;

import lombok.Builder;
import lombok.Data;

/**
 * github_test
 * Created on 13.07.17.
 */

@Data
@Builder
public class Repo {
    private long id;
    private String name;
    private String gitNick;
}
