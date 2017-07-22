package com.epam.testsystem.github.web.controller;

import com.epam.testsystem.github.TestUtil;
import com.epam.testsystem.github.model.Repo;
import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * github_test
 * Created on 06.07.17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles(SPRING_PROFILE_TEST)
public class UserRestControllerTest {
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TestUtil testUtil;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    @Transactional
    public void findAll() throws Exception {
        final User user = testUtil.getMainUser();
        final Repo repo = testUtil.addRepo();

        final Task task1 = testUtil.addTask(repo.getId(), user.getId());
        final Task task2 = testUtil.addTask(repo.getId(), user.getId());

        mockMvc.perform(get("/user")
                .accept(contentType)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email", is(user.getEmail())))
                .andExpect(jsonPath("$[0].gitNick", is(user.getGitNick())))
                .andExpect(jsonPath("$[0].tasks[0].successful", is(task1.isSuccessful())))
                .andExpect(jsonPath("$[0].tasks[0].log", notNullValue()))
                .andExpect(jsonPath("$[0].tasks[1].successful", is(task2.isSuccessful())))
                .andExpect(jsonPath("$[0].tasks[1].log", notNullValue()));
    }
}