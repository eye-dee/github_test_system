package com.epam.testsystem.github.web.controller;

import com.epam.testsystem.github.TestUtil;
import com.epam.testsystem.github.model.Repo;
import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.User;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by antonnazarov on 13.07.17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles(SPRING_PROFILE_TEST)
public class RepoRestControllerTest {
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
    public void findAllByUserIdRepoId() throws Exception {
        final User user = testUtil.getMainUser();
        final Repo repo1 = testUtil.addRepo();
        final Repo repo2 = testUtil.addRepo();
        final Task task1 = testUtil.addTask(user.getId(), repo1.getId(), false, "log1");
        final Task task2 = testUtil.addTask(user.getId(), repo1.getId(), false, "log2");
        final Task task3 = testUtil.addTask(user.getId(), repo2.getId(), false, "log3");


        mockMvc.perform(get("/repo/%d" + repo1.getId())
                .accept(contentType)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].log", is(task1.getLog())))
                .andExpect(jsonPath("$[1].log", is(task2.getLog())));

        mockMvc.perform(get("/repo/" + repo2.getId())
                .accept(contentType)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].log", is(task3.getLog())));
    }
}