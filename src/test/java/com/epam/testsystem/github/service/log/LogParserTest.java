package com.epam.testsystem.github.service.log;

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
import java.util.List;
import java.util.Map;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(parsedLog.getClones())
            .allMatch(str -> str.startsWith("$ git clone https://gitlab.com/MortyMerr/private_tests.git $TEST_DIR/tests"));

        final Map<String, List<String>> cycles = parsedLog.getCycles();
        assertThat(cycles.get("jar")).hasSize(0);
        assertThat(cycles.get("classes")).hasSize(0);

        assertThat(parsedLog.getBuildResult()).startsWith("BUILD FAILED");
        assertThat(cycles.get("test").get(1).contains("findMax FAILED")).isTrue();
    }

    @Test
    public void parseSuccess() throws Exception{
        final String test_log = readRsFile("test_log_success");

        final GradleLog parsedLog = logParser.getStructedLog(test_log);
        assertThat(parsedLog.getBuildResult()).startsWith("BUILD SUCCESSFUL");
        assertThat(parsedLog.getCycles().get("test")).isEmpty();
    }
}