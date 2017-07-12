package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.TestUtil;
import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.model.UserWithTasks;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    @Autowired
    private TestUtil testUtil;

    @Test
    @Transactional
    public void add() throws Exception {
        final String email = "EMAIL";
        final String githubNick = "github_nick";
        final String password = "password";

        assertThat(userDao.add(email, githubNick, password))
                .satisfies(
                        u -> {
                            assertThat(u.getEmail()).isEqualTo(email);
                            assertThat(u.getGithubNick()).isEqualTo(githubNick);
                            assertThat(u.getPassword()).isEqualTo(password);
                            assertThat(u.getId()).isGreaterThan(0);
                        }
                );
    }

    @Test
    @Transactional
    @Sql(statements = {
            "INSERT INTO users(id, email, github_nick, password) VALUES(1000, 'email', 'githubNick', 'password')"
    })
    public void findById() throws Exception {
        final User user = User.builder().id(1000).email("email").githubNick("githubNick").password("password").build();
        assertThat(userDao.findById(user.getId())).contains(user);
    }

    public void findByIdNotExists() {
        assertThat(userDao.findById(0)).isEmpty();
    }

    @Test
    @Transactional
    @Sql(statements = {
            "INSERT INTO users(id, email, github_nick, password) VALUES(1000, 'email', 'githubNick', 'password')"
    })
    public void findByEmail() throws Exception {
        final User user = User.builder().id(1000).email("email").githubNick("githubNick").password("password").build();
        assertThat(userDao.findByEmail("email")).contains(user);
    }

    public void findByEmailNotExists() {
        assertThat(userDao.findByEmail("")).isEmpty();
    }

    @Test
    @Transactional
    public void findAllWithTasks() {
        final User user1 = testUtil.getMainUser();
        final User user2 = testUtil.makeUser();

        final Task task11 = testUtil.addTask(user1.getId());

        final Task task21 = testUtil.addTask(user2.getId());
        final Task task22 = testUtil.addTask(user2.getId());
        final Task task23 = testUtil.addTask(user2.getId());

        final List<UserWithTasks> tmp = userDao.findAllWithTasks();

        assertThat(userDao.findAllWithTasks())
                .hasSize(2)
                .containsOnlyOnce(
                        UserWithTasks.builder()
                                .user(user1)
                                .tasks(Collections.singletonList(task11))
                                .build(),
                        UserWithTasks.builder()
                                .user(user2)
                                .tasks(Arrays.asList(task21, task22, task23))
                                .build()
                );
    }
}