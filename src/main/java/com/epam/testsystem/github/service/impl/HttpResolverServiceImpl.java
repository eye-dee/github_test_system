package com.epam.testsystem.github.service.impl;

import com.epam.testsystem.github.exception.BusinessLogicException;
import com.epam.testsystem.github.service.HttpResolverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

/**
 * github_test
 * Created on 05.07.17.
 */

@Service
public class HttpResolverServiceImpl implements HttpResolverService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpResolverServiceImpl.class);

    @Override
    public <T> T sendGETRequest(final String url, final Class<T> type) {
        LOGGER.debug("GET with url={}", url);
        return executeGETRequest(url, HttpEntity.EMPTY, type);
    }

    @Override
    public <T> T sendGETRequestWithHeaders(final String url, Map<String, String> headers, final Class<T> type) {
        LOGGER.debug("GET with url={} and headers", url);
        if (!CollectionUtils.isEmpty(headers)) {
            HttpHeaders httpHeaders = new HttpHeaders();
            headers.forEach((headerName, headerValue) -> httpHeaders.set(headerName, headerValue));
            return executeGETRequest(url, new HttpEntity(httpHeaders), type);
        } else {
            return sendGETRequest(url, type);
        }
    }

    private <T> T executeGETRequest(String url, HttpEntity entity, Class<T> type) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<T> out = restTemplate.exchange(url, HttpMethod.GET, entity, type);
            if (!Objects.equals(out.getStatusCode(), HttpStatus.OK)) {
                LOGGER.error("Incorrect response status code = {} instead of code = 200", out.getStatusCode().toString());
                throw new BusinessLogicException("Incorreсt response status code ".concat(out.getStatusCode().toString()).concat(" instead of code = 200"));
            }
            return out.getBody();
        } catch (Exception e) {
            LOGGER.error("Can't GET to: {} because of {}", url, e.getMessage());
            throw new BusinessLogicException("Can't GET to " + url + "  because of : " + e.getMessage());
        }
    }
}