package com.epam.testsystem.github.service.checker;

import com.epam.testsystem.github.TestUtil;
import com.epam.testsystem.github.dao.TaskDao;
import com.epam.testsystem.github.enums.TaskStatus;
import com.epam.testsystem.github.model.Repo;
import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.service.log.GitHubStatusResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * github_test
 * Create on 06.07.17.
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

    private void checkStatusSuccessful(long taskId, TaskStatus statusRequired, boolean successfulRequired) {
        assertThat(jdbcTemplate.queryForObject("SELECT status FROM tasks WHERE id = ?",
                new Object[]{taskId},
                String.class)).isEqualTo(statusRequired.name());

        assertThat(jdbcTemplate.queryForObject("SELECT successful FROM tasks WHERE id = ?",
                new Object[]{taskId},
                Boolean.class)).isEqualTo(successfulRequired);
    }

    @Test
    @Transactional
    public void checkTask() throws Exception {
        when(gitHubStatusResolver.getUserResult(anyString(), anyString(), anyString())).thenReturn(true, false);
        final Repo repo = testUtil.addRepo();

        final User user = testUtil.makeUser();
        final Task task1 = taskDao.add(user.getId(), repo.getId());
        final Task task2 = taskDao.add(user.getId(), repo.getId());

        assertThat(taskDao.findAllInProgress()).hasSize(2);

        checkGitHubService.check();

        checkStatusSuccessful(task1.getId(), TaskStatus.CHECKED, true);
        checkStatusSuccessful(task2.getId(), TaskStatus.CHECKED, false);

    }

    @Test
    @Transactional
    public void checkTaskWithException() throws Exception {
        when(gitHubStatusResolver.getUserResult(anyString(), anyString(), anyString())).thenReturn(true).thenThrow(new RuntimeException("test"));
        final Repo repo = testUtil.addRepo();

        final User user = testUtil.makeUser();
        final Task task1 = taskDao.add(user.getId(), repo.getId());
        final Task task2 = taskDao.add(user.getId(), repo.getId());

        assertThat(taskDao.findAllInProgress()).hasSize(2);

        checkGitHubService.check();
        checkStatusSuccessful(task1.getId(), TaskStatus.CHECKED, true);
        checkStatusSuccessful(task2.getId(), TaskStatus.CHECKED, false);

    }
}