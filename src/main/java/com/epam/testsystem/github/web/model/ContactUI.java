package com.epam.testsystem.github.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactUI {
    private long id;
    private long userId;
    private String type;
    private String inf;
    private boolean enabled;
}
