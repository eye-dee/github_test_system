package com.epam.testsystem.github.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * github_test
 * Created on 08.07.17.
 */

@Service
@RequiredArgsConstructor
public class TravisLogsResolver {
    private static final String REQUEST = "https://api.travis-ci.org/jobs/" +
            "%d/" + //<- job_id (calculate as build_id + 1)
            "log";
    private final HttpResolverService httpResolverServiceImpl;

    public String getLogs(long buildId) {
        return httpResolverServiceImpl.sendGETRequest(String.format(REQUEST, buildId + 1), String.class);
    }
}
