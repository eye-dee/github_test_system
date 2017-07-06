package com.epam.testsystem.github.sevice;

import com.epam.testsystem.github.TestUtil;
import com.epam.testsystem.github.dao.TaskDao;
import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.TaskStatus;
import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.service.CheckGitHubService;
import com.epam.testsystem.github.service.GitHubStatusResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by antonnazarov on 06.07.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
public class CheckGitHubServiceTest {

    @Autowired
    private TestUtil testUtil;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private CheckGitHubService checkGitHubService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private GitHubStatusResolver gitHubStatusResolver;

    @Test
    @Transactional
    public void checkTask() throws Exception {
        when(gitHubStatusResolver.getUserResult(anyString(), anyString(), anyString())).thenReturn(true, false);

        final User user = testUtil.makeUser();
        final Task task1 = taskDao.add(user.getId());
        final Task task2 = taskDao.add(user.getId());

        assertThat(taskDao.findAllInProgress()).hasSize(2);

        checkGitHubService.check();

        assertThat(jdbcTemplate.queryForObject("SELECT status FROM tasks WHERE id = ?",
                new Object[]{task1.getId()},
                String.class)).isEqualTo(TaskStatus.CHECKED.name());

        assertThat(jdbcTemplate.queryForObject("SELECT successful FROM tasks WHERE id = ?",
                new Object[]{task1.getId()},
                Boolean.class)).isEqualTo(true);

        assertThat(jdbcTemplate.queryForObject("SELECT status FROM tasks WHERE id = ?",
                new Object[]{task2.getId()},
                String.class)).isEqualTo(TaskStatus.CHECKED.name());

        assertThat(jdbcTemplate.queryForObject("SELECT successful FROM tasks WHERE id = ?",
                new Object[]{task2.getId()},
                Boolean.class)).isEqualTo(false);
    }

    @Test
    @Transactional
    public void checkTaskWithException() throws Exception {
        when(gitHubStatusResolver.getUserResult(anyString(), anyString(), anyString())).thenReturn(true).thenThrow(new RuntimeException("test"));

        final User user = testUtil.makeUser();
        final Task task1 = taskDao.add(user.getId());
        final Task task2 = taskDao.add(user.getId());

        assertThat(taskDao.findAllInProgress()).hasSize(2);

        checkGitHubService.check();

        assertThat(jdbcTemplate.queryForObject("SELECT status FROM tasks WHERE id = ?",
                new Object[]{task1.getId()},
                String.class)).isEqualTo(TaskStatus.CHECKED.name());

        assertThat(jdbcTemplate.queryForObject("SELECT successful FROM tasks WHERE id = ?",
                new Object[]{task1.getId()},
                Boolean.class)).isEqualTo(true);

        assertThat(jdbcTemplate.queryForObject("SELECT status FROM tasks WHERE id = ?",
                new Object[]{task2.getId()},
                String.class)).isEqualTo(TaskStatus.CHECKED.name());

        assertThat(jdbcTemplate.queryForObject("SELECT successful FROM tasks WHERE id = ?",
                new Object[]{task2.getId()},
                Boolean.class)).isEqualTo(false);
    }
}