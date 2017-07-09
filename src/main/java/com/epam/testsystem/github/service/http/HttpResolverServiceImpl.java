package com.epam.testsystem.github.service.http;

import com.epam.testsystem.github.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * github_test
 * Created on 05.07.17.
 */

@Service
@RequiredArgsConstructor
public class HttpResolverServiceImpl implements HttpResolverService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpResolverService.class);

    private final RestTemplate restTemplate;

    public <T> T sendGETRequest(final String url, final Class<T> type) {
        LOGGER.debug("GET with url={}", url);
        final HttpEntity entity = HttpEntity.EMPTY;
        try {
            final ResponseEntity<T> out = restTemplate.exchange(url, HttpMethod.GET, entity, type);
            if (!Objects.equals(out.getStatusCode(), HttpStatus.OK)) {
                LOGGER.error("Incorrect response status code = {} instead of code = 200", out.getStatusCode().toString());
                throw new BusinessLogicException("Incorreсt response status code ".concat(out.getStatusCode().toString()).concat(" instead of code = 200"));
            }
            return out.getBody();
        } catch (final Exception e) {
            LOGGER.error("Can't GET to: {} because of {}", url, e.getMessage());
            throw new BusinessLogicException("Can't GET to " + url + "  because of : " + e.getMessage());
        }
    }


}