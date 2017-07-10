package com.epam.testsystem.github.service;

import java.util.Map;

/**
 * @author <a href="mailto:Daria_Makarova@epam.com">Daria Makarova</a>
 * @version 0.1
 * @since 0.1
 */

public interface HttpResolverService {
    <T> T sendGETRequest(String url, Class<T> type);

    <T> T sendGETRequestWithHeaders(String url, Map<String, String> headers, Class<T> type);

}
