package com.epam.testsystem.github.service.http;

import java.util.Map;

/**
 * github_test
 * Created on 09.07.17.
 */
public interface HttpResolverService {

    void setCredentials(final String username, final String password);

    <T> T sendGETRequest(final String url, final Class<T> type);

    <T> T sendGETRequestWithCredentials(final String url, final Class<T> type);

    <T> T sendGETRequestWithHeaders(final String url, final Map<String, String> headers, final Class<T> type);

    <T> T sendGETRequestWithHeadersAndCredentials(final String url, Map<String, String> headers, final Class<T> type);
}
