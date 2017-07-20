package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.TestUtil;
import com.epam.testsystem.github.enums.TaskStatus;
import com.epam.testsystem.github.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

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
    public void addOrUpdate() throws Exception {
        final User user = testUtil.makeUser();
        final Repo repo = testUtil.addRepo();
        assertThat(taskDao.addOrUpdate(user.getId(), repo.getId(), false, "{}"))
                .satisfies(
                        task -> {
                            assertThat(task.getId()).isGreaterThan(0);
                            assertThat(task.getRegisterTime()).isBeforeOrEqualTo(LocalDateTime.now());
                            assertThat(task.getUserId()).isEqualTo(user.getId());
                            assertThat(task.getLog()).isEqualTo("{}");
                            assertThat(task.isSuccessful()).isFalse();
                        }
                );
    }

    @Test
    @Ignore
    @Transactional
    public void update() throws Exception {
        final User user = testUtil.makeUser();
        final Repo repo = testUtil.addRepo();

        final long id = taskDao.addOrUpdate(user.getId(), repo.getId(), false, "oldLog").getId();

        assertThat(taskDao.addOrUpdate(user.getId(), repo.getId(), false, "newLog").getId())
                .isEqualTo(id);
        assertThat(taskDao.findAllByUserId(user.getId())).hasSize(1);
        assertThat(taskDao.findAllByUserId(user.getId()).get(0).getLog()).isEqualTo("newLog");

    }

    @Test
    @Transactional
    @Sql(statements = {
            "INSERT INTO users(id, email, git_nick, password) VALUES(1000, 'email', 'github_nick', 'password')",
            "INSERT INTO tasks(user_id, register_time, log) VALUES(1000, CURRENT_TIMESTAMP, '{}')",
            "INSERT INTO tasks(user_id, register_time, log) VALUES(1000, CURRENT_TIMESTAMP, '{}')",
            "INSERT INTO tasks(user_id, register_time, log) VALUES(1000, CURRENT_TIMESTAMP, '{}')"
    })
    public void findAllInProgress() throws Exception {
        assertThat(taskDao.findAllByUserId(1000))
                .hasSize(3);
    }

    @Test
    @Transactional
    public void findAllWithCertainCycle() throws Exception{
        final User user = testUtil.makeUser();
        final Repo repo = testUtil.addRepo();
        final GradleLog gradleLog = new GradleLog();
        gradleLog.getCycles().put("abc", Collections.singletonList("ecd"));
        final ObjectMapper objectMapper = new ObjectMapper();
        final String log = objectMapper.writeValueAsString(gradleLog);


        final Task task = taskDao.addOrUpdate(user.getId(), repo.getId(), false, log);
        assertThat(taskDao.findAllByUserId(user.getId(),"cycles").get(0).getLog())
                .isEqualTo("{\"abc\": [\"ecd\"]}");
    }

    @Test
    @Transactional
    public void add() {
        final User mainUser = testUtil.getMainUser();
        final Repo repo = testUtil.addRepo();

        final Task task = taskDao.add(mainUser.getId(), repo.getId());
        assertThat(task)
                .satisfies(t -> {
                    assertThat(t.getId()).isGreaterThan(0);
                    assertThat(t.getLog()).isEqualTo("{}");
                    assertThat(t.getRegisterTime()).isAfterOrEqualTo(LocalDateTime.now().withNano(0));
                    assertThat(t.getStatus()).isEqualTo(TaskStatus.PROGRESS);
                    assertThat(t.getRepoId()).isEqualTo(repo.getId());
                    assertThat(t.getUserId()).isEqualTo(mainUser.getId());
                });


        assertThat(taskDao.findAllByUserId(task.getUserId()))
                .containsOnly(task);

    }

    @Test
    @Transactional
    public void setResultById() throws Exception {
        final User user = testUtil.getMainUser();
        final Repo repo = testUtil.addRepo();
        final Task task = taskDao.addOrUpdate(user.getId(), repo.getId(), false, "{}");

        assertThat(taskDao.findAllByUserId(user.getId()))
                .hasSize(1)
                .allMatch(t -> t.getUserId() == user.getId());

        assertThat(taskDao.setResultById(user.getId(), task.getId(), false, "{}")).isTrue();
        assertThat(taskDao.findAllByUserId(user.getId()).get(0).getLog()).isEqualTo("{}");
    }

    @Test
    @Transactional
    public void setResultByIdNotExists() throws Exception {
        assertThat(taskDao.setResultById(0, 0, false, "{}")).isFalse();
    }

    @Test
    @Transactional
    public void findByUserIdRepoIdWithAppliedFilters() {
        final User user1 = testUtil.makeUser();
        final User user2 = testUtil.makeUser();
        final Repo repo1 = testUtil.addRepo();

        taskDao.addOrUpdate(user1.getId(), repo1.getId(), false, "{}");
        taskDao.addOrUpdate(user1.getId(), repo1.getId(), false, "{}");
        taskDao.addOrUpdate(user1.getId(), repo1.getId(), true, "{}");

        taskDao.addOrUpdate(user2.getId(), repo1.getId(), true, "{}");

        assertThat(taskDao.findByUserIdRepoIdWithAppliedFilters(user1.getId(), repo1.getId(), 10, false, true)).hasSize(2);

        assertThat(taskDao.findByUserIdRepoIdWithAppliedFilters(user1.getId(), repo1.getId(), 3, false, false)).hasSize(3);

        assertThat(taskDao.findByUserIdRepoIdWithAppliedFilters(user1.getId(), repo1.getId(), 1, false, false)).hasSize(1);

        assertThat(taskDao.findByUserIdRepoIdWithAppliedFilters(user2.getId(), repo1.getId(), 100, false, true)).hasSize(0);
    }

}