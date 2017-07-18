package com.epam.testsystem.github.web.controller;

import com.epam.testsystem.github.TestUtil;
import com.epam.testsystem.github.dao.RepoDao;
import com.epam.testsystem.github.model.Repo;
import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.web.model.NewRepoUI;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * github_test
 * Create on 13.07.17.
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

    @Autowired
    private RepoDao repoDao;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    @Transactional
    public void addNewRepo() throws Exception {
        final User user = testUtil.getMainUser();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String newRepoJson = objectMapper.writeValueAsString(
                NewRepoUI.builder()
                        .id(1)
                        .name("repo")
                        .owner_id(user.getId())
                        .build()
        );

        mockMvc.perform(post("/repo/new")
                .accept(contentType)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newRepoJson))
                .andExpect(status().isOk());

        assertThat(repoDao.findByOwner(user.getId())).hasSize(1);
    }

    @Test
    @Transactional
    public void findAllByUserIdRepoId() throws Exception {
        final User user = testUtil.getMainUser();
        final Repo repo1 = testUtil.addRepo();
        final Repo repo2 = testUtil.addRepo();
        final Task task1 = testUtil.addTask(user.getId(), repo1.getId(), false, "{}");
        final Task task2 = testUtil.addTask(user.getId(), repo1.getId(), false, "{}");
        final Task task3 = testUtil.addTask(user.getId(), repo2.getId(), false, "{}");


        mockMvc.perform(get("/repo/" + repo1.getId())
                .accept(contentType)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].successful", is(task1.isSuccessful())))
                .andExpect(jsonPath("$[1].successful", is(task2.isSuccessful())))
                .andExpect(jsonPath("$[0].log", notNullValue()))
                .andExpect(jsonPath("$[1].log", notNullValue()));

        mockMvc.perform(get("/repo/" + repo2.getId())
                .accept(contentType)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].successful", is(task3.isSuccessful())))
                .andExpect(jsonPath("$[0].log", notNullValue()));
    }
}