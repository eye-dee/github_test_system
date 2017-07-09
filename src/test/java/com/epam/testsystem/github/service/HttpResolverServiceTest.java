package com.epam.testsystem.github.service;

import com.epam.testsystem.github.service.http.HttpResolverService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Autowired
    private HttpResolverService httpResolverService;

    @Test
    public void successHttpGetRequest() {
        assertThat(httpResolverService.sendGETRequest(GITHUB_STATUSES_URL, String.class), is(notNullValue()));
    }
}
