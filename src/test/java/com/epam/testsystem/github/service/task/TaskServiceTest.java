package com.epam.testsystem.github.service.task;

import com.epam.testsystem.github.dao.TaskDao;
import com.epam.testsystem.github.exception.BusinessLogicException;
import com.epam.testsystem.github.service.log.LogParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.mockito.Mockito.mock;

/**
 * @author <a href="mailto:Daria_Makarova@epam.com">Daria Makarova</a>
 * @version 0.1
 * @since 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
public class TaskServiceTest {

    private TaskService taskService;
    private TaskDao taskDao;
    private LogParser logParser;

    @Before
    public void setUp() {
        taskDao = mock(TaskDao.class);
        logParser = mock(LogParser.class);
        taskService = new TaskServiceImpl(taskDao, logParser);
    }

    @Test(expected = BusinessLogicException.class)
    public void failedToGetLessThanNullTasksInResult() {
        taskService.findByUserIdRepoIdWithAppliedFilters(1L, 1L, -5, false, false);
    }
}
