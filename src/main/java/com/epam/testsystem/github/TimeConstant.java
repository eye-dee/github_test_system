package com.epam.testsystem.github;

import java.time.format.DateTimeFormatter;

/**
 * github_test
 * Created on 06.07.17.
 */
public interface TimeConstant {
    String FORMAT = "yyyy-HH-MM";
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(FORMAT);
}
