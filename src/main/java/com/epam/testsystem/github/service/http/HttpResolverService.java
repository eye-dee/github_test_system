package com.epam.testsystem.github.service.http;

import java.util.Map;

/**
 * github_test
 * Created on 09.07.17.
 */
public interface HttpResolverService {

    <T> T sendGETRequest(final String url, final Class<T> type);

    <T> T sendGETRequestWithHeaders(final String url, final String username, final String password, final Map<String, String> headers, final Class<T> type);
}
