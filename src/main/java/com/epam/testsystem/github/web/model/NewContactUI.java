package com.epam.testsystem.github.web.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewContactUI {
    private long userId;
    private String type;
    private String inf;
}
