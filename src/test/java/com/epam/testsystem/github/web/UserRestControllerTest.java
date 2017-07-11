package com.epam.testsystem.github.web;

import com.epam.testsystem.github.TestUtil;
import com.epam.testsystem.github.dao.UserDao;
import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.web.model.NewUserUI;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    private UserDao userDao;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    @Transactional
    public void register() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final String newUserJson = objectMapper.writeValueAsString(
                NewUserUI.builder()
                        .email("email")
                        .githubNick("githubNick")
                        .build()
        );

        mockMvc.perform(post("/user/register")
                .accept(contentType)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson))
                .andExpect(status().isOk());

        assertThat(userDao.findByEmail("email"))
                .hasValueSatisfying(
                        user -> {
                            assertThat(user.getId()).isGreaterThan(0);
                            assertThat(user.getEmail()).isEqualTo("email");
                            assertThat(user.getGithubNick()).isEqualTo("githubNick");
                        }
                );
    }

    @Test
    @Transactional
    public void findAll() throws Exception {
        final User user = testUtil.makeUser();
        final Task task1 = testUtil.addTask(user.getId());
        final Task task2 = testUtil.addTask(user.getId());

        mockMvc.perform(get("/user")
                .accept(contentType)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email", is(user.getEmail())))
                .andExpect(jsonPath("$[0].githubNick", is(user.getGithubNick())))
                .andExpect(jsonPath("$[0].tasks[0].successful", is(task1.isSuccessful())))
                .andExpect(jsonPath("$[0].tasks[0].log", is(task1.getLog())))
                .andExpect(jsonPath("$[0].tasks[1].successful", is(task2.isSuccessful())))
                .andExpect(jsonPath("$[0].tasks[1].log", is(task1.getLog())));
    }
}