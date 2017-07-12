package com.epam.testsystem.github.service.logs;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by antonnazarov on 12.07.17.
 */

@Service
public class LogParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogParser.class);
    private static final List<String> CYCLES = Arrays.asList(
            ":compileJava\n",
            ":processResources ",
            ":classes\n",
            ":jar\n",
            ":assemble\n",
            ":compileTestJava\n",
            ":processTestResources ",
            ":testClasses\n",
            ":test\n",
            "BUILD");

    private List<String> getLineWith(final String log, final String word) {
        Matcher            m       = Pattern.compile(String.format("(?m)%s.*", word)).matcher(log);
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

    public JSONObject getStructedLog(final String log) {
        try {
            JSONObject jsonLog = new JSONObject()
                    .put("clone", getLineWith(log, "\\$ git clone"));
            for (int i = 0; i < CYCLES.size() - 1; i++) {
                jsonLog.put(CYCLES.get(i).substring(1, CYCLES.get(i).length() - 1), getCycleInf(log, CYCLES.get(i), CYCLES.get(i + 1)));
            }
            jsonLog.put("BUILD", getLineWith(log, "BUILD"));
            return jsonLog;
        } catch (JSONException e) {
            LOGGER.error("Error while parsing log\n" + e.getMessage());
        }
        return null;
    }
}
