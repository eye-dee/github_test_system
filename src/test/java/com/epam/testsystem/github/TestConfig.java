package com.epam.testsystem.github;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_DEV;
import static org.mockito.Mockito.mock;

/**
 * github_test
 * Created on 05.07.17.
 */

@Configuration
public class TestConfig {

    @Bean
    public FlywayMigrationStrategy cleanMigrationStrategy() {
        return flyway -> {
            flyway.clean();
            flyway.migrate();
        };
    }

    @Bean
    @Primary
    @Profile(SPRING_PROFILE_DEV)
    public TestUtil testUtil() {
        return mock(TestUtil.class);
    }
}
