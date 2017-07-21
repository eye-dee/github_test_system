package com.epam.testsystem.github.service.log;

import com.epam.testsystem.github.model.GradleLog;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * github_test
 * Create on 12.07.17.
 */

@Service
public class LogParser {
    public GradleLog getStructedLog(final String log) {
        final String[] splitted = log.replace("\r","").split("\n");
        final GradleLog structedLog = new GradleLog();
        String lastCycle = "";

        for (final String aSplitted : splitted) {
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
