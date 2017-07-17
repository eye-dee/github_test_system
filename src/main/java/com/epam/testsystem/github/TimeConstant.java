package com.epam.testsystem.github;

import java.time.format.DateTimeFormatter;

/**
 * github_test
 * Created on 06.07.17.
 */
public interface TimeConstant {
    String FORMAT = "yyyy-MM-dd HH:mm:ss";
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(FORMAT);
}
