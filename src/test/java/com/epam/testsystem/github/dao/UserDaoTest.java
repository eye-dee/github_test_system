package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * github_test
 * Created on 05.07.17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    @Transactional
    public void add() throws Exception {
        final String email = "EMAIL";
        final String githubNick = "github_nick";
        assertThat(userDao.add(email, githubNick))
                .satisfies(
                        u -> {
                            assertThat(u.getEmail()).isEqualTo(email);
                            assertThat(u.getGithubNick()).isEqualTo(githubNick);
                            assertThat(u.getId()).isGreaterThan(0);
                        }
                );

    }

    @Test
    @Transactional
    @Sql(statements = {
            "INSERT INTO users(id, email, github_nick) VALUES(1, 'email', 'githubNick')"
    })
    public void findById() throws Exception {
        final User user = User.builder().id(1).email("email").githubNick("githubNick").build();
        assertThat(userDao.findById(user.getId())).contains(user);
    }

    public void findByIdNotExists() {
        assertThat(userDao.findById(0)).isEmpty();
    }

}