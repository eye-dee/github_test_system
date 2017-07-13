package com.epam.testsystem.github.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class GradleLog {
    private Map<String, List<String>> cycles = new HashMap<>();
    private List<String> clones = new ArrayList<>();
    private String buildResult = "";
}
