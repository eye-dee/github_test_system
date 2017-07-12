package com.epam.testsystem.github.service.logs;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

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

        final JSONObject parsedLog = logParser.getStructedLog(test_log);
        assertEquals(parsedLog.getString("clone"),
                "[$ git clone https://gitlab.com/MortyMerr/private_tests.git $TEST_DIR/tests]");
        assertEquals(parsedLog.getString("compileJava"), ":compileJava\n");
        assertEquals(parsedLog.getString("processResources"), ":processResources UP-TO-DATE\n");
        assertEquals(parsedLog.getString("classes"), ":classes\n");
        assertEquals(parsedLog.getString("jar"), ":jar\n");
        assertEquals(parsedLog.getString("assemble"), ":assemble\n");
        assertEquals(parsedLog.getString("compileTestJava"), ":compileTestJava\n");
        assertEquals(parsedLog.getString("processTestResources"), ":processTestResources UP-TO-DATE\n");
        assertEquals(parsedLog.getString("testClasses"), ":testClasses\n");
        assertEquals(parsedLog.getString("BUILD"), "[BUILD FAILED]");
        assertEquals(parsedLog.getString("testClasses"), ":testClasses\n");
        assertEquals(parsedLog.getString("test"), ":test\n\n" +
                "MainTest > findMax FAILED\n" +
                "    java.lang.UnsupportedOperationException at MainTest.java:14\n" +
                "\n" +
                "MainTest > findMin FAILED\n" +
                "    java.lang.UnsupportedOperationException at MainTest.java:19\n" +
                "\n" +
                "MathTestImprove > findMax FAILED\n" +
                "    java.lang.IllegalArgumentException at MathTestImprove.java:42\n" +
                "\n" +
                "MathTestImprove > findMin FAILED\n" +
                "    java.lang.IllegalArgumentException at MathTestImprove.java:42\n" +
                "\n" +
                "4 tests completed, 4 failed\n" +
                ":test FAILED\n" +
                "\n" +
                "FAILURE: Build failed with an exception.\n" +
                "\n" +
                "* What went wrong:\n" +
                "Execution failed for task ':test'.\n" +
                "> There were failing tests. See the report at: file:///builds/Igor_Drozdov1/gitlab-test-system/build/reports/tests/test/index.html\n" +
                "\n" +
                "* Try:\n" +
                "Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output.\n" +
                "\n");
    }

    @Test
    public void parseSuccess() throws Exception{
        final String test_log = readRsFile("test_log_success");


        final JSONObject parsedLog = logParser.getStructedLog(test_log);
        assertEquals(parsedLog.getString("clone"),
                "[$ git clone --depth=50 https://github.com/epamtestsystem/java_knowledge.git epamtestsystem/java_knowledge, $ git clone $TEST_PACKAGE tests]");
        assertEquals(parsedLog.getString("compileJava"), ":compileJava\n");
        assertEquals(parsedLog.getString("processResources"), ":processResources UP-TO-DATE\n");
        assertEquals(parsedLog.getString("classes"), ":classes\n");
        assertEquals(parsedLog.getString("jar"), ":jar\n");
        assertEquals(parsedLog.getString("processTestResources"), ":processTestResources UP-TO-DATE\n");
        assertEquals(parsedLog.getString("testClasses"), ":testClasses\n");
        assertEquals(parsedLog.getString("BUILD"), "[BUILD SUCCESSFUL, BUILD SUCCESSFUL, BUILD SUCCESSFUL]");
        assertEquals(parsedLog.getString("testClasses"), ":testClasses\n");
    }

}