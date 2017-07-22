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
        assertThat(approvementDao.add(task.getId(), operator.getId()))
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
        approvementDao.add(task.getId(), operator.getId());
        assertThatThrownBy(() -> approvementDao.add(task.getId(), operator.getId()))
                .hasCauseExactlyInstanceOf(SQLIntegrityConstraintViolationException.class);
    }

    @Test
    @Transactional
    public void find() throws Exception {
        final Approvement approvement = approvementDao.add(task.getId(), operator.getId());

        assertThat(approvementDao.find(task.getId(), operator.getId()))
                .hasValueSatisfying(c -> assertThat(c).isEqualTo(approvement));
    }

    @Test
    @Transactional
    public void update() throws Exception {
        approvementDao.add(task.getId(), operator.getId());

        assertThat(approvementDao.update(task.getId(), operator.getId(), ApprovementStatus.GOOD))
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