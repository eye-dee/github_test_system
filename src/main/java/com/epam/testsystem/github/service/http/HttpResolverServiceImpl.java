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
    private String authorizationHeaderValue;

    @Override
    public void setCredentials(final String username, final String password) {
        LOGGER.debug("setCredentials with username={} and password=[PROTECTED]", username, password);
        authorizationHeaderValue = new StringBuilder("Basic ").append(createAuthHeader(username, password)).toString();
    }

    @Override
    public <T> T sendGETRequest(final String url, final Class<T> type) {
        LOGGER.debug("sendGETRequest with url={}", url);
        return executeGETRequest(url, HttpEntity.EMPTY, type);
    }

    @Override
    public <T> T sendGETRequestWithCredentials(final String url, final Class<T> type) {
        LOGGER.debug("sendGETRequestWithCredentials with url={}", url);
        if (!StringUtils.hasText(authorizationHeaderValue)) {
            LOGGER.error("There is no credentials. Please set credentials before calling method");
            throw new BusinessLogicException("There is no credential. Please set credentials before calling method");
        }
        return executeGETRequest(url, new HttpEntity(getHeaderWithCredentials()), type);
    }

    @Override
    public <T> T sendGETRequestWithHeaders(final String url, final Map<String, String> headers, final Class<T> type) {
        LOGGER.debug("sendGETRequestWithHeaders with url={} and headers", url);
        if (!CollectionUtils.isEmpty(headers)) {
            final HttpHeaders httpHeaders = new HttpHeaders();
            headers.forEach(httpHeaders::set);
            return executeGETRequest(url, new HttpEntity(httpHeaders), type);
        } else {
            LOGGER.error("There are no any headers. Please call the method with not null headers as a parameter.");
            throw new BusinessLogicException("There are no any headers. Please call the method with not null headers as a parameter");
        }
    }

    @Override
    public <T> T sendGETRequestWithHeadersAndCredentials(final String url, Map<String, String> headers, final Class<T> type) {
        LOGGER.debug("sendGETRequestWithHeadersAndCredentials with url={} and headers", url);
        if (StringUtils.hasText(authorizationHeaderValue) && !CollectionUtils.isEmpty(headers)) {
            headers.put(HttpHeaders.AUTHORIZATION, authorizationHeaderValue);
            return sendGETRequestWithHeaders(url, headers, type);
        } else {
            LOGGER.error("There is no credentials or headers. Please set credentials before calling method");
            throw new BusinessLogicException("There is no credentials or headers. Please set credentials before calling method");
        }
    }

    private String createAuthHeader(final String username, final String password) {
        final String auth = username + ":" + password;
        final byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")));
        return new String(encodedAuth);
    }

    private HttpHeaders getHeaderWithCredentials() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, authorizationHeaderValue);
        return httpHeaders;
    }

    private <T> T executeGETRequest(String url, HttpEntity entity, Class<T> type) {
        try {
            final ResponseEntity<T> out = restTemplate.exchange(url, HttpMethod.GET, entity, type);
            if (!Objects.equals(out.getStatusCode(), HttpStatus.OK)) {
                LOGGER.error("Incorrect response status code = {} instead of code = 200", out.getStatusCode().toString());
                throw new BusinessLogicException(new StringBuilder("Incorrect response status code ")
                        .append(out.getStatusCode())
                        .append(" instead of code = 200")
                        .toString());
            }
            return out.getBody();
        } catch (Exception e) {
            LOGGER.error("Can't GET to: {} because of {}", url, e.getMessage());
            throw new BusinessLogicException(new StringBuilder("Can't GET to ")
                    .append(url)
                    .append("  because of : ")
                    .append(e.getMessage())
                    .toString());
        }
    }
}