package com.epam.testsystem.github.service.travis;


import com.epam.testsystem.github.service.log.TravisLogsResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
public class TravisLogsResolverTest {

    private static final long BUILD_ID = 250141992; //initial pull request

    @Autowired
    private TravisLogsResolver travisLogsResolver;

    @Test
    public void getBuildLog() {
        final String logs = travisLogsResolver.getLogs("", BUILD_ID);
        assertThat(logs.substring(0, 15))
                .isEqualTo("travis_fold:start:worker_info".substring(0, 15));
        assertThat(logs.indexOf("The command \"./gradlew build\" exited with 1."))
                .isNotEqualTo(-1);
    }

}