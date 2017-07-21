package com.epam.testsystem.github.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Contact {
    private long id;
    private long userId;
    private String type;
    private String inf;
    private boolean enabled;
}
