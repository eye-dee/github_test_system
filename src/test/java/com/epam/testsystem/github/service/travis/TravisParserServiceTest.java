package com.epam.testsystem.github.service.travis;

import com.epam.testsystem.github.dao.UserDao;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * github_test
 * Create on 7/10/2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
public class TravisParserServiceTest {
    private static final String AUTHOR_EMAIL = "daniel.buch@gmail.com";

    @Autowired
    private TravisParserService travisParserService;

    @Autowired
    private UserDao userDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    public void parseSuccessful() throws Exception {
        String payload = FileUtils.readFileToString(
                new File("src/test/resources/travis_payload.json"), "UTF-8"
        );

        assertThat(travisParserService.parse(payload)).isTrue();

        assertThat(userDao.findByEmail(AUTHOR_EMAIL)).isNotEmpty();
        assertTaskAmount(1);
    }

    @Test
    @Transactional
    public void parseFailed() throws Exception {
        String payload = "";

        assertThat(travisParserService.parse(payload)).isFalse();

        assertTaskAmount(0);
    }

    private void assertTaskAmount(int amount) {
        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tasks", Integer.class)).isEqualTo(amount);
    }
}