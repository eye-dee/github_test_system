package com.epam.testsystem.github.service.log;

import com.epam.testsystem.github.service.log.GitlabLogsResolver;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
public class GitlabLogsResolverTest {
    final private static int BUILD_ID = 48148;


    @Autowired
    private GitlabLogsResolver gitlabLogsResolver;

    @Test
    public void getLogs() throws Exception {
        final ClassLoader classLoader = getClass().getClassLoader();
        final String expected = FileUtils.readFileToString(
                new File(classLoader.getResource("test_log_failed").getFile()),
                "UTF-8");

        final String logs = gitlabLogsResolver.getLogs("Igor_Drozdov1", BUILD_ID);
        assertEquals(logs, expected.replace("\r", ""));
    }
}