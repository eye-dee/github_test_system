package com.epam.testsystem.github.service.log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * github_test
 * Created on 12.07.17.
 */

@Service
public class GitlabLogsResolver implements LogResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitlabLogsResolver.class);
    private final String url =
            "https://git.epam.com/" +
                    "%s" + //<- user
                    "/gitlab-test-system/builds/" +
                    "%d/" + //<- build_id
                    "?private_token=%s";
    @Value("${gitlab.private_token}")
    private String privateToken;

    public String getLogs(final String user, final long buildId) {
        try {
            final Document doc = Jsoup.connect(String.format(url, user, buildId, privateToken)).get();
            final Elements select = doc.select("pre.trace");
            return select.text();
        } catch (IOException e) {
            LOGGER.error("Error while parsing log\n" + e.getMessage());
        }
        return "";
    }
}
