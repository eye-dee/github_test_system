package com.epam.testsystem.github.service;

/**
 * github_test
 * Created on 09.07.17.
 */
public interface HttpResolverService {
    <T> T sendGETRequest(final String url, final Class<T> type);
}
