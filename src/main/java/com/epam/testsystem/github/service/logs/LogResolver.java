package com.epam.testsystem.github.service.logs;

/**
 * Created by antonnazarov on 12.07.17.
 */
public interface LogResolver {
    String getLogs(final long buildId);
}
