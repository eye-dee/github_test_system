package com.epam.testsystem.github.service;

import com.epam.testsystem.github.exception.BusinessLogicException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author <a href="mailto:Daria_Makarova@epam.com">Daria Makarova</a>
 * @version 0.1
 * @since 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
public class HttpResolverServiceTest {

    private static final String GITHUB_STATUSES_URL = "https://api.github.com/repos/epamtestsystem/java_knowledge/statuses/2358303f938827fae7a4dfd4e882bc2886feb50c";
    private static final String TRAVIS_CI_API_URL_FOR_OPEN_SOURCE = "https://api.travis-ci.org";
    private static final String TRAVIS_CI_API_URL_FOR_GET_ALL_BUILDS = "/repos/epamtestsystem/java_knowledge/builds";
    private static final String TRAVIS_CI_API_HEADER_ACCEPT = "application/vnd.travis-ci.2+json";
    private static final String GITHUB_ACCOUNT_USERNAME = "epamtestsystem";
    private static final String CORRECT_GITHUB_ACCOUNT_PASSWORD = "password12345";
    private static final String INCORRECT_GITHUB_ACCOUNT_PASSWORD = "hgfhaggfayg7648";

    @Autowired
    private HttpResolverService httpResolverServiceImpl;

    @Test
    public void successHttpGetRequestWithCredentialsToGitHubAPI() {
        String authEncoded = Base64.getEncoder().encodeToString((GITHUB_ACCOUNT_USERNAME.concat(":").concat(CORRECT_GITHUB_ACCOUNT_PASSWORD)).getBytes());
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + (authEncoded));
        assertThat(httpResolverServiceImpl.sendGETRequestWithHeaders(GITHUB_STATUSES_URL, headers, String.class), is(notNullValue()));
    }

    @Test(expected = BusinessLogicException.class)
    public void failedHttpGetRequestWithCredentialsToGitHubAPI() {
        String authEncoded = Base64.getEncoder().encodeToString((GITHUB_ACCOUNT_USERNAME.concat(":").concat(INCORRECT_GITHUB_ACCOUNT_PASSWORD)).getBytes());
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + (authEncoded));
        httpResolverServiceImpl.sendGETRequestWithHeaders(GITHUB_STATUSES_URL, headers, String.class);
    }

    @Test
    public void successHttpGetRequestToTravisAPIForBuilds() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", TRAVIS_CI_API_HEADER_ACCEPT);
        assertThat(httpResolverServiceImpl.sendGETRequest(TRAVIS_CI_API_URL_FOR_OPEN_SOURCE.concat(TRAVIS_CI_API_URL_FOR_GET_ALL_BUILDS), String.class), is(notNullValue()));
    }

}
