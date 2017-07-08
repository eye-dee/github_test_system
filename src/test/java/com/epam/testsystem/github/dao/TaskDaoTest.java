package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.TestUtil;
import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.TaskStatus;
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
        assertThat(taskDao.add(user.getId(), 1))
            .satisfies(
                    task -> {
                        assertThat(task.getId()).isGreaterThan(0);
                        assertThat(task.getRegisterTime()).isBeforeOrEqualTo(LocalDateTime.now());
                        assertThat(task.getUserId()).isEqualTo(user.getId());
                        assertThat(task.getStatus()).isEqualTo(TaskStatus.PROGRESS);
                    }
            );
    }

    @Test
    @Transactional
    @Sql(statements = {
            "INSERT INTO users(id, email, github_nick) VALUES(1, 'email', 'github_nick')",
            "INSERT INTO tasks(user_id, register_time, status) VALUES(1, CURRENT_TIMESTAMP, 'PROGRESS')",
            "INSERT INTO tasks(user_id, register_time, status) VALUES(1, CURRENT_TIMESTAMP, 'PROGRESS')",
            "INSERT INTO tasks(user_id, register_time, status) VALUES(1, CURRENT_TIMESTAMP, 'CHECKED')"
    })
    public void findAllInProgress() throws Exception {
        assertThat(taskDao.findAllInProgress())
                .hasSize(2)
                .allMatch(task -> task.getStatus() == TaskStatus.PROGRESS);
    }

    @Test
    @Transactional
    public void setResultById() throws Exception {
        final User user = testUtil.makeUser();
        final Task task = taskDao.add(user.getId(), 1);

        assertThat(taskDao.findAllInProgress())
                .hasSize(1)
                .allMatch(t -> t.getUserId() == user.getId());

        assertThat(taskDao.setResultById(user.getId(), task.getId(), false, task.getLog())).isTrue();
        assertThat(taskDao.findAllInProgress()).hasSize(0);
    }

    @Test
    @Transactional
    public void setResultByIdNotExists() throws Exception {
        assertThat(taskDao.setResultById(0, 0, false, "")).isFalse();
    }
}