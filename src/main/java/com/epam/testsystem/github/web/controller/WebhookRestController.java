package com.epam.testsystem.github.web.controller;

import com.epam.testsystem.github.service.travis.TravisParserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * github_test
 * Created on 08.07.17.
 */

@RestController
@RequestMapping("webhook")
@RequiredArgsConstructor
public class WebhookRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebhookRestController.class);

    private final TravisParserService travisParserService;


    @RequestMapping(value = "travisci", method = RequestMethod.POST)
    public boolean newPullTravis(@RequestBody final String payload) {
        LOGGER.info("Triggered webhook from travis");

        return travisParserService.parse(payload);
    }

    @RequestMapping(value = "gitlabci", method = RequestMethod.POST)
    public boolean newPullGitlab(@RequestBody final String payload) {
        LOGGER.info("Triggered webhook from gitlab");

        return true;
    }
}
