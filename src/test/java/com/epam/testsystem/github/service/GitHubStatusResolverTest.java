package com.epam.testsystem.github.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * github_test
 * Created on 06.07.17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
public class GitHubStatusResolverTest {
    private static final String USER_LOGIN = "sausageRoll";
    private static final String OTHER_USER_LOGIN = "epamtestsystem";

    @Autowired
    private GitHubStatusResolver gitHubStatusResolver;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private JsonNode pulls;
    private JsonNode userPull;
    private JsonNode otherUserPull;

    private String failureStatus;
    private String successStatus;

    @Before
    public void setUp() throws IOException {
        String pullsJson = FileUtils.readFileToString(
                new File("src/test/resources/github_pulls_response.json"), "UTF-8"
        );
        pulls = objectMapper.readTree(pullsJson);
        userPull = gitHubStatusResolver.getUserPullRequest(pulls, USER_LOGIN).get();
        otherUserPull = gitHubStatusResolver.getUserPullRequest(pulls, OTHER_USER_LOGIN).get();
        failureStatus = FileUtils.readFileToString(
                new File("src/test/resources/github_statuses_failure_response.json"),"UTF-8"
        );

        successStatus = FileUtils.readFileToString(
                new File("src/test/resources/github_statuses_success_response.json"), "UTF-8"
        );
    }

    @Test
    public void resolveStatus() throws Exception {
        assertThat(gitHubStatusResolver.resolveStatus(failureStatus)).isFalse();
        assertThat(gitHubStatusResolver.resolveStatus(successStatus)).isTrue();

    }

    @Test
    public void isSuccessFalse() throws Exception {
        assertThat(gitHubStatusResolver.isSuccess("failure")).isFalse();
    }

    @Test
    public void isSuccessTrue() throws Exception {
        assertThat(gitHubStatusResolver.isSuccess("success")).isTrue();
    }

    @Test
    public void getUserPullRequest() throws Exception {
        assertThat(gitHubStatusResolver.getUserPullRequest(pulls, USER_LOGIN))
                .hasValueSatisfying(
                        jsonNode -> assertThat(jsonNode.get("user").get("login").asText()).isEqualTo(USER_LOGIN)
                );
    }

    @Test
    public void pullContainToUser() throws Exception {
        assertThat(gitHubStatusResolver.pullContainToUser(userPull, USER_LOGIN)).isTrue();
        assertThat(gitHubStatusResolver.pullContainToUser(otherUserPull, USER_LOGIN)).isFalse();
    }

}