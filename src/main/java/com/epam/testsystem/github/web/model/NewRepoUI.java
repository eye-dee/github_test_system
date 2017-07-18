package com.epam.testsystem.github.web.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewRepoUI {
    private long id;
    private String name;
    private long owner_id;
}
