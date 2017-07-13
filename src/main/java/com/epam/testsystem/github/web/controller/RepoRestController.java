package com.epam.testsystem.github.web.controller;

import com.epam.testsystem.github.web.model.TaskUI;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * github_test
 * Create on 7/13/2017.
 */

@RestController
@RequestMapping("repo")
public class RepoRestController {

    @RequestMapping(value = "{repoId}", method = RequestMethod.GET)
    public List<TaskUI> getTasks(@RequestParam(value = "repoId") final long repoId) {
        return null;
    }

}
