package com.epam.testsystem.github.service.http;

import com.epam.testsystem.github.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;

/**
 * github_test
 * Created on 05.07.17.
 */

@Service
@RequiredArgsConstructor
public class HttpResolverServiceImpl implements HttpResolverService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpResolverServiceImpl.class);

    private final RestTemplate restTemplate;

    @Override
    public <T> T sendGETRequest(final String url, final Class<T> type) {
        LOGGER.debug("GET with url={}", url);
        return executeGETRequest(url, HttpEntity.EMPTY, type);
    }

    @Override
    public <T> T sendGETRequestWithHeaders(final String url, final String username, final String password, final Map<String, String> headers, final Class<T> type) {
        LOGGER.debug("GET with url={}", url);
        final HttpHeaders httpHeaders = new HttpHeaders();
        if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
            httpHeaders.set("Authorization", "Basic ".concat(createAuthHeader(username, password)));
        }
        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach(httpHeaders::set);
        }
        return executeGETRequest(url, new HttpEntity(httpHeaders), type);
    }

    private String createAuthHeader(String username, String password) {
        final String auth = username + ":" + password;
        final byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")));
        return new String(encodedAuth);
    }

    private <T> T executeGETRequest(String url, HttpEntity entity, Class<T> type) {
        try {
            final ResponseEntity<T> out = restTemplate.exchange(url, HttpMethod.GET, entity, type);
            if (!Objects.equals(out.getStatusCode(), HttpStatus.OK)) {
                LOGGER.error("Incorrect response status code = {} instead of code = 200", out.getStatusCode().toString());
                throw new BusinessLogicException("Incorrect response status code ".concat(out.getStatusCode().toString()).concat(" instead of code = 200"));
            }
            return out.getBody();
        } catch (Exception e) {
            LOGGER.error("Can't GET to: {} because of {}", url, e.getMessage());
            throw new BusinessLogicException("Can't GET to " + url + "  because of : " + e.getMessage());
        }
    }
}