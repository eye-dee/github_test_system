package com.epam.testsystem.github.service.http;

import com.epam.testsystem.github.exception.BusinessLogicException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Autowired
    private HttpResolverService httpResolverServiceImpl;

    @MockBean
    private RestTemplate restTemplate;
    private ResponseEntity mockResponse;

    @Before
    public void setUp() {
        mockResponse = mock(ResponseEntity.class);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);
    }

    @Test
    public void sendGETRequestWithCredentialsWithNullAuthorizationHeaderValue() {
        httpResolverServiceImpl.sendGETRequestWithCredentials("aaa", String.class);
    }

    @Test
    public void successHttpGetRequest() {
        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.OK);
        when(mockResponse.getBody()).thenReturn("body");

        assertThat(httpResolverServiceImpl.sendGETRequest(GITHUB_STATUSES_URL, String.class), is("body"));
    }

    @Test
    public void exceptionHttpGetRequest() {
        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);

        assertThatThrownBy(() ->
                httpResolverServiceImpl.sendGETRequest(GITHUB_STATUSES_URL, String.class)
        ).isInstanceOf(BusinessLogicException.class);
    }

    @Test
    public void successHttpGetRequestWithCredentials() {
        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.OK);
        when(mockResponse.getBody()).thenReturn("body");

        httpResolverServiceImpl.setCredentials("name", "password");
        assertThat(httpResolverServiceImpl.sendGETRequestWithCredentials(GITHUB_STATUSES_URL, String.class), is("body"));
    }

    @Test
    public void exceptionHttpGetRequestWithCredentials() {
        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);

        assertThatThrownBy(() ->
                httpResolverServiceImpl.sendGETRequestWithCredentials(GITHUB_STATUSES_URL, String.class)
        ).isInstanceOf(BusinessLogicException.class);
    }

    @Test
    public void successHttpGetRequestWithHeaders() {
        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.OK);
        when(mockResponse.getBody()).thenReturn("body");

        Map<String, String> headersMap = Collections.singletonMap("Accept", MediaType.APPLICATION_JSON_VALUE);
        assertThat(httpResolverServiceImpl.sendGETRequestWithHeaders(GITHUB_STATUSES_URL,
                headersMap, String.class), is("body"));
    }

    @Test
    public void exceptionHttpGetRequestWithHeaders() {
        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.FORBIDDEN);

        assertThatThrownBy(() ->
                httpResolverServiceImpl.sendGETRequestWithHeaders(GITHUB_STATUSES_URL, null, String.class)
        ).isInstanceOf(BusinessLogicException.class);
    }

    @Test
    public void successHttpGetRequestWithHeadersAndCredentials() {
        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.OK);
        when(mockResponse.getBody()).thenReturn("body");

        httpResolverServiceImpl.setCredentials("correct", "correct");
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Accept", MediaType.APPLICATION_JSON_VALUE);
        assertThat(httpResolverServiceImpl.sendGETRequestWithHeadersAndCredentials(GITHUB_STATUSES_URL,
                headersMap, String.class), is("body"));
    }

    @Test
    public void exceptionHttpGetRequestWithHeadersAndCredentials() {
        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.FORBIDDEN);

        assertThatThrownBy(() ->
                httpResolverServiceImpl.sendGETRequestWithHeadersAndCredentials(GITHUB_STATUSES_URL,
                        null, String.class)
        ).isInstanceOf(BusinessLogicException.class);
    }
}
