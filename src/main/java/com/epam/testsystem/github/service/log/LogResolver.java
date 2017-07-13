package com.epam.testsystem.github.service.log;

/**
 * github_test
 * Create on 12.07.17.
 */
public interface LogResolver {
    String getLogs(String user, final long buildId);
}
