package com.epam.testsystem.github.service.logs;

/**
 * github_test
 * Create on 12.07.17.
 */
public interface LogResolver {
    String getLogs(String user, final long buildId);
}
