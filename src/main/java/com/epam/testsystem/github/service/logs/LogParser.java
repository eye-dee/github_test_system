package com.epam.testsystem.github.service.logs;

import com.epam.testsystem.github.model.GradleLog;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by antonnazarov on 12.07.17.
 */

@Service
public class LogParser {
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
