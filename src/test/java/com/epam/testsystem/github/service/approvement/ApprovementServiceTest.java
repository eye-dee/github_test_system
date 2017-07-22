package com.epam.testsystem.github.service.approvement;

import com.epam.testsystem.github.dao.ApprovementDao;
import com.epam.testsystem.github.enums.ApprovementStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
public class ApprovementServiceTest {
    private static final long ANY_TASK_ID = 1;
    private static final long ANY_USER_ID = 1;
    @Autowired
    private ApprovementService approvementService;

    @MockBean
    private ApprovementDao approvementDao;

    @Test
    public void add() throws Exception {
        approvementDao.add(ANY_TASK_ID, ANY_USER_ID);

        verify(approvementDao, times(1)).add(eq(ANY_TASK_ID), eq(ANY_USER_ID));
    }

    @Test
    public void find() throws Exception {
        approvementService.find(ANY_USER_ID, ANY_TASK_ID);

        verify(approvementDao, times(1)).find(eq(ANY_USER_ID), eq(ANY_TASK_ID));
    }

    @Test
    public void findAll() throws Exception {
        approvementService.find(ANY_TASK_ID);

        verify(approvementDao, times(1)).find(eq(ANY_TASK_ID));
    }

    @Test
    public void markGood() throws Exception {
        approvementService.markGood(ANY_USER_ID, ANY_TASK_ID);

        verify(approvementDao, times(1)).update(ANY_USER_ID, ANY_TASK_ID, ApprovementStatus.GOOD);
    }

    @Test
    public void markBad() throws Exception {
        approvementService.markBad(ANY_USER_ID, ANY_TASK_ID);

        verify(approvementDao, times(1)).update(ANY_USER_ID, ANY_TASK_ID, ApprovementStatus.BAD);
    }
}