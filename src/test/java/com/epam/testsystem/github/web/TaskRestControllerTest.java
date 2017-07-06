package com.epam.testsystem.github.web;

import com.epam.testsystem.github.TestUtil;
import com.epam.testsystem.github.dao.TaskDao;
import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.web.model.NewTaskUI;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * github_test
 * Created on 06.07.17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles(SPRING_PROFILE_TEST)
public class TaskRestControllerTest {
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TestUtil testUtil;

    @Autowired
    private TaskDao taskDao;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    @Transactional
    public void addNewTask() throws Exception {
        final User user = testUtil.makeUser();
        final ObjectMapper objectMapper = new ObjectMapper();

        final String newTaskJson = objectMapper.writeValueAsString(
                NewTaskUI.builder()
                        .email(user.getEmail())
                        .githubNick(user.getGithubNick())
                        .build()
        );

        mockMvc.perform(post("/task/new")
                .accept(contentType)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTaskJson))
                .andExpect(status().isOk());

        assertThat(taskDao.findAllInProgress())
                .hasSize(1);
    }
}