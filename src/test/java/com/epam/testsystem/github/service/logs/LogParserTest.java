package com.epam.testsystem.github.service.logs;

import com.epam.testsystem.github.model.GradleLog;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
public class LogParserTest {

    @Autowired
    private LogParser logParser;

    private String readRsFile(final String name) throws IOException {
        final ClassLoader classLoader = getClass().getClassLoader();
        return FileUtils.readFileToString(
                new File(classLoader.getResource(name).getFile()),
                "UTF-8");
    }

    @Test
    public void parseFailed() throws Exception {
        final String test_log = readRsFile("test_log_failed");

        final GradleLog parsedLog = logParser.getStructedLog(test_log);
        assertEquals(parsedLog.getClones(),
                Collections.singletonList("$ git clone https://gitlab.com/MortyMerr/private_tests.git $TEST_DIR/tests"));
        final Map<String, List<String>> cycles = parsedLog.getCycles();
        assert(cycles.get("jar").isEmpty());
        assert(cycles.get("classes").isEmpty());
        assertEquals(parsedLog.getBuildResult(), "BUILD FAILED");
        assert(cycles.get("test").get(1).contains("findMax FAILED"));
    }

    @Test
    public void parseSuccess() throws Exception{
        final String test_log = readRsFile("test_log_success");

        final GradleLog parsedLog = logParser.getStructedLog(test_log);
        assertEquals(parsedLog.getBuildResult(), "BUILD SUCCESSFUL");
        assert(parsedLog.getCycles().get("test").isEmpty());
    }
}