package com.epam.testsystem.github.service.task;

import com.epam.testsystem.github.dao.TaskDao;
import com.epam.testsystem.github.exception.BusinessLogicException;
import com.epam.testsystem.github.model.GradleLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * github_test
 * Create on 7/19/2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
public class TaskServiceImplTest {
    private static final long ANY_USER_ID = 1;
    private static final String ANY_LOG = "{}";
    private static final long ANY_REPO_ID = 1;
    private static final boolean ANY_SUCCESS = true;
    private static final long ANY_TASK_ID = 1;
    private static final int ANY_MAX_TASKS_IN_RESULT = 1;

    @MockBean
    TaskDao taskDao;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private TaskService taskService;

    @Test
    public void addOrUpdate() throws Exception {
        taskService.addOrUpdate(ANY_USER_ID, ANY_REPO_ID, ANY_SUCCESS, ANY_LOG);

        final String log = OBJECT_MAPPER.writeValueAsString(new GradleLog());

        verify(taskDao, times(1)).addOrUpdate(ANY_USER_ID, ANY_REPO_ID, ANY_SUCCESS, log);
    }

    @Test
    public void findAllByUserId() throws Exception {
        taskService.findAllByUserId(ANY_USER_ID);

        verify(taskDao, times(1)).findAllByUserId(ANY_USER_ID);
    }

    @Test
    public void setResultById() throws Exception {
        taskService.setResultById(ANY_USER_ID, ANY_TASK_ID, ANY_SUCCESS, ANY_LOG);

        verify(taskDao, times(1)).setResultById(ANY_USER_ID, ANY_TASK_ID, ANY_SUCCESS, ANY_LOG);
    }

    @Test
    public void findByUserIdRepoIdWithAppliedFiltersTrueTrue() throws Exception {
        taskService.findByUserIdRepoIdWithAppliedFilters(ANY_USER_ID, ANY_REPO_ID, ANY_MAX_TASKS_IN_RESULT, true, true);

        verify(taskDao, times(1)).findByUserIdRepoIdWithAppliedFilters(ANY_USER_ID, ANY_REPO_ID, ANY_MAX_TASKS_IN_RESULT, false, false);
    }

    @Test
    public void findByUserIdRepoIdWithAppliedFiltersTrueFalse() throws Exception {
        taskService.findByUserIdRepoIdWithAppliedFilters(ANY_USER_ID, ANY_REPO_ID, ANY_MAX_TASKS_IN_RESULT, true, false);

        verify(taskDao, times(1)).findByUserIdRepoIdWithAppliedFilters(ANY_USER_ID, ANY_REPO_ID, ANY_MAX_TASKS_IN_RESULT, true, false);
    }

    @Test
    public void findByUserIdRepoIdWithAppliedFiltersFalseTrue() throws Exception {
        taskService.findByUserIdRepoIdWithAppliedFilters(ANY_USER_ID, ANY_REPO_ID, ANY_MAX_TASKS_IN_RESULT, false, true);

        verify(taskDao, times(1)).findByUserIdRepoIdWithAppliedFilters(ANY_USER_ID, ANY_REPO_ID, ANY_MAX_TASKS_IN_RESULT, false, true);
    }

    @Test
    public void findByUserIdRepoIdWithAppliedFiltersFalseFalse() throws Exception {
        taskService.findByUserIdRepoIdWithAppliedFilters(ANY_USER_ID, ANY_REPO_ID, ANY_MAX_TASKS_IN_RESULT, false, false);

        verify(taskDao, times(1)).findByUserIdRepoIdWithAppliedFilters(ANY_USER_ID, ANY_REPO_ID, ANY_MAX_TASKS_IN_RESULT, false, false);
    }

    @Test
    public void findByUserIdRepoIdWithAppliedFiltersRaiseException() {
        assertThatThrownBy(
                () -> taskService.findByUserIdRepoIdWithAppliedFilters(
                        ANY_USER_ID, ANY_REPO_ID, -1, ANY_SUCCESS, ANY_SUCCESS)
        )
                .isInstanceOf(BusinessLogicException.class)
                .hasMessageMatching(".*maxTasksInResultReturn.*");

        assertThatThrownBy(
                () -> taskService.findByUserIdRepoIdWithAppliedFilters(
                        ANY_USER_ID, ANY_REPO_ID, 5_001, ANY_SUCCESS, ANY_SUCCESS)
        )
                .isInstanceOf(BusinessLogicException.class)
                .hasMessageMatching(".*maxTasksInResultReturn.*");
    }

    @Test
    public void add() throws Exception {
        taskService.add(ANY_USER_ID, ANY_REPO_ID);

        verify(taskDao, times(1)).add(ANY_USER_ID, ANY_REPO_ID);
    }

    @Test
    public void findAllInProgress() throws Exception {
        taskService.findAllInProgress();

        verify(taskDao, times(1)).findAllInProgress();
    }

}