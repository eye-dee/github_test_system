package com.epam.testsystem.github.service.logs;

import com.epam.testsystem.github.model.GradleLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by antonnazarov on 12.07.17.
 */

@Service
public class LogParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogParser.class);

    private List<String> getLineWith(final String log, final String word) {
        Matcher m = Pattern.compile(String.format("(?m)%s.*", word)).matcher(log);
        final List<String> founded = new ArrayList<>();
        while (m.find()) {
            founded.add(m.group(0));
        }
        return founded;
    }

    private String getCycleInf(final String log, final String left, final String right) {
        final int leftPos = log.indexOf(left);
        return log.substring(leftPos, log.indexOf(right, leftPos));
    }

    public GradleLog getStructedLog(final String log) {
        String[] splitted = log.split("\n");
        GradleLog structedLog = new GradleLog();
        String lastCycle = "";

        for (String aSplitted : splitted) {
            if (aSplitted.contains("git clone")) {
                structedLog.getClones().add(aSplitted);
            } else if (aSplitted.startsWith(":")) {
                lastCycle = aSplitted.substring(1, aSplitted.length());
                structedLog.getCycles().putIfAbsent(lastCycle, new ArrayList<>());
            } else if (aSplitted.contains("BUILD")) {
                structedLog.setBuildResult(aSplitted);
            } else if (!lastCycle.isEmpty()) {
                structedLog.getCycles().get(lastCycle).add(aSplitted);
            }
        }
        return structedLog;
    }
}
