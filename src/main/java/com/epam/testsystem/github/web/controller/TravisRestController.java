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
@RequestMapping("travisci")
@RequiredArgsConstructor
public class TravisRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TravisRestController.class);

    private final TravisParserService travisParserService;

    @RequestMapping(method = RequestMethod.POST)
    public boolean newPull(@RequestBody final String payload) {
        LOGGER.info("Triggered by webhook");

        return travisParserService.parse(payload);
    }
}
