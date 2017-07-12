package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.TestUtil;
import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * github_test
 * Created on 05.07.17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
public class TaskDaoTest {

    @Autowired
    private TestUtil testUtil;

    @Autowired
    private TaskDao taskDao;

    @Test
    @Transactional
    public void add() throws Exception {
        final User user = testUtil.makeUser();
        assertThat(taskDao.addOrUpdate(user.getId(), 0, false, "log"))
            .satisfies(
                    task -> {
                        assertThat(task.getId()).isGreaterThan(0);
                        assertThat(task.getRegisterTime()).isBeforeOrEqualTo(LocalDateTime.now());
                        assertThat(task.getUserId()).isEqualTo(user.getId());
                        assertThat(task.getLog()).isEqualTo("log");
                        assertThat(task.isSuccessful()).isFalse();
                    }
            );
    }

    @Test
    @Transactional
    public void update() throws Exception {
        final User user = testUtil.makeUser();

        final long id = taskDao.addOrUpdate(user.getId(), 0, false, "oldLog").getId();

        assertThat(taskDao.addOrUpdate(user.getId(), 0, false, "newLog").getId())
                .isEqualTo(id);
        assertThat(taskDao.findAllByUserId(user.getId())).hasSize(1);
        assertThat(taskDao.findAllByUserId(user.getId()).get(0).getLog()).isEqualTo("newLog");

    }

    @Test
    @Transactional
    @Sql(statements = {
            "INSERT INTO users(id, email, github_nick, password) VALUES(1000, 'email', 'github_nick', 'password')",
            "INSERT INTO tasks(user_id, register_time) VALUES(1000, CURRENT_TIMESTAMP)",
            "INSERT INTO tasks(user_id, register_time) VALUES(1000, CURRENT_TIMESTAMP)",
            "INSERT INTO tasks(user_id, register_time) VALUES(1000, CURRENT_TIMESTAMP)"
    })
    public void findAllInProgress() throws Exception {
        assertThat(taskDao.findAllByUserId(1000))
                .hasSize(3);
    }

    @Test
    @Transactional
    public void setResultById() throws Exception {
        final User user = testUtil.getMainUser();
        final Task task = taskDao.addOrUpdate(user.getId(), 0, false, "");

        assertThat(taskDao.findAllByUserId(user.getId()))
                .hasSize(1)
                .allMatch(t -> t.getUserId() == user.getId());

        assertThat(taskDao.setResultById(user.getId(), task.getId(), false, "log")).isTrue();
        assertThat(taskDao.findAllByUserId(user.getId()).get(0).getLog()).isEqualTo("log");
    }

    @Test
    @Transactional
    public void setResultByIdNotExists() throws Exception {
        assertThat(taskDao.setResultById(0, 0, false, "")).isFalse();
    }
}