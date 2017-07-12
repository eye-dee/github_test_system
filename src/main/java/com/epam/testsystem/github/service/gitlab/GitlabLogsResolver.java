package com.epam.testsystem.github.service.gitlab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * github_test
 * Created on 12.07.17.
 */

@Service
public class GitlabLogsResolver {

    // TODO: 12.07.17 set it to application.properties
    @Value("${gitlab.private_token}")
    private String privateToken;

    // TODO: 12.07.17 You need to get logs by build id
    // Find out about log parser libraries. Slava told about that.
    // What can we do with these libraries and do we need it?
    // This test has access to the Internet
    // GitlabLogsResolver and TravisLogsResolver have the same structure.
    // Make the package logs, move all LogsResolver to it and make the common interface

    public String getLogs(final long buildId) {
        return null;
    }
}
