package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.TestUtil;
import com.epam.testsystem.github.enums.ApprovementStatus;
import com.epam.testsystem.github.model.Approvement;
import com.epam.testsystem.github.model.Repo;
import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
public class ApprovementDaoTest {
    @Autowired
    private ApprovementDao approvementDao;

    @Autowired
    private TestUtil testUtil;

    private User operator;
    private Task task;

    @Before
    @Transactional
    public void setUp() {
        operator = testUtil.makeOperator();
        final Repo repo = testUtil.addRepo();
        task = testUtil.addTask(repo.getId(), operator.getId());
    }

    @Test
    @Transactional
    public void add() throws Exception {
        assertThat(approvementDao.add(operator.getId(), task.getId()))
                .satisfies(
                        a -> {
                            assertThat(a.getUserId()).isEqualTo(operator.getId());
                            assertThat(a.getTaskId()).isEqualTo(task.getId());
                            assertThat(a.getApprove_time()).isBeforeOrEqualTo(LocalDateTime.now());
                            assertThat(a.getMark()).isEqualTo(ApprovementStatus.VIEWED);
                        }
                );
    }

    @Test
    @Transactional
    public void addRepeated() throws Exception {
        approvementDao.add(operator.getId(), task.getId());
        assertThatThrownBy(() -> approvementDao.add(operator.getId(), task.getId()))
                .hasCauseExactlyInstanceOf(SQLIntegrityConstraintViolationException.class);
    }

    @Test
    @Transactional
    public void find() throws Exception {
        final Approvement approvement = approvementDao.add(operator.getId(), task.getId());

        assertThat(approvementDao.find(operator.getId(), task.getId()))
                .hasValueSatisfying(c -> assertThat(c).isEqualTo(approvement));
    }

    @Test
    @Transactional
    public void findAll() throws Exception {
        final User operator2 = testUtil.makeOperator();
        approvementDao.add(operator.getId(), task.getId());
        approvementDao.add(operator2.getId(), task.getId());

        assertThat(approvementDao.find(task.getId())).hasSize(2);
    }

    @Test
    @Transactional
    public void update() throws Exception {
        approvementDao.add(operator.getId(), task.getId());

        assertThat(approvementDao.update(operator.getId(), task.getId(), ApprovementStatus.GOOD))
                .satisfies(
                        a -> {
                            assertThat(a.getUserId()).isEqualTo(operator.getId());
                            assertThat(a.getTaskId()).isEqualTo(task.getId());
                            assertThat(a.getApprove_time()).isBeforeOrEqualTo(LocalDateTime.now());
                            assertThat(a.getMark()).isEqualTo(ApprovementStatus.GOOD);
                        }
                );
    }

}