package com.epam.testsystem.github.service;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * github_test
 * Created on 08.07.17.
 */

@Service
public class TravisLogsResolver {
    private static String REQUEST = "https://travis-ci.org/epamtestsystem/java_knowledge/builds/%s";
    
    public String getLogs(long buildId) {
        // TODO: 08.07.17 get plain text
        try {
            Jsoup.connect(String.format(REQUEST, buildId)).get();
        } catch (IOException e) {
            // TODO: 08.07.17 handle 
        }
        return null;
    }
}
