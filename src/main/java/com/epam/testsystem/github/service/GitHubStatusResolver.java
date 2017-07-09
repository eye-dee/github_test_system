package com.epam.testsystem.github.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

/**
 * github_test
 * Created on 05.07.17.
 */

@Service
@RequiredArgsConstructor
public class GitHubStatusResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubStatusResolver.class);
    private static final String PULLS_REQUEST = "https://api.github.com/repos/" +
            "%s/" + //<- user name
            "%s/" + //repository name
            "pulls";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final HttpResolverService httpResolverServiceImpl;

    public boolean getUserResult(final String userLogin, final String owner, final String repo) {
        final String allRepos = getAllRepos(owner, repo);
        final Optional<String> statusesUrl = getStatusesUrl(allRepos, userLogin);

        if (statusesUrl.isPresent()) {
            final String statuses = httpResolverServiceImpl.sendGETRequest(statusesUrl.get(),String.class);
            return resolveStatus(statuses);
        } else {
            LOGGER.info("User {} didn't make pull", userLogin);
            return false;
        }
    }

    protected boolean resolveStatus(final String statuses) {
        try {
            final JsonNode jsonStatuses = OBJECT_MAPPER.readTree(statuses);
            if (jsonStatuses.size() <= 0) {
                return false;
            }
            return isSuccess(jsonStatuses.get(0).get("state").asText());
        } catch (final IOException e) {
            LOGGER.error("Can't parse statuses response\nWith error {}", e.getMessage());
        }
        return false;
    }

    protected boolean isSuccess(final String status) {
        return status.equals("success");
    }

    protected Optional<String> getStatusesUrl(final String json, final String userLogin) {
        try {
            final JsonNode allPullRequests = OBJECT_MAPPER.readTree(json);
            final Optional<JsonNode> userPullRequest = getUserPullRequest(allPullRequests, userLogin);

            if (userPullRequest.isPresent()) {
                return Optional.of(userPullRequest.get().get("statuses_url").asText());
            } else {
                return Optional.empty();
            }

        } catch (final IOException e) {
            LOGGER.error("Can't parse json\nWith error {}", e.getMessage());
            return Optional.empty();
        }
    }

    private String getAllRepos(final String owner, final String repo) {
        return httpResolverServiceImpl.sendGETRequest(
                String.format(PULLS_REQUEST,owner, repo), String.class
        );
    }

    protected Optional<JsonNode> getUserPullRequest(final JsonNode allPullRequests, final String userLogin) {
        for (int i = 0; i < allPullRequests.size(); ++i) {
            final JsonNode currentPull = allPullRequests.get(i);
            if (pullContainToUser(currentPull, userLogin)) {
                return Optional.of(currentPull);
            }
        }
        return Optional.empty();
    }

    protected boolean pullContainToUser(final JsonNode pull, final String userLogin) {
        return pull.get("user").get("login").asText().equals(userLogin);
    }
}
