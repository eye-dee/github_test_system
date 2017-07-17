package com.epam.testsystem.github;

import com.epam.testsystem.github.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_DEV;
import static org.mockito.Mockito.mock;

/**
 * github_test
 * Created on 05.07.17.
 */

@Configuration
public class TestConfig {
    @Autowired
    private TestUtil testUtil;

    @Bean
    public FlywayMigrationStrategy cleanMigrationStrategy() {
        return flyway -> {
            flyway.clean();
            flyway.migrate();

            final User user = testUtil.makeMainUser();
            if (user != null) {
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getAuthorities()));
            }
        };
    }

    @Bean
    @Primary
    @Profile(SPRING_PROFILE_DEV)
    public TestUtil testUtil() {
        return mock(TestUtil.class);
    }
}
