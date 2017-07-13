package com.epam.testsystem.github.web.controller;

import com.epam.testsystem.github.dao.UserDao;
import com.epam.testsystem.github.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Optional;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * github_test
 * Created on 08.07.17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles(SPRING_PROFILE_TEST)
public class TravisRestControllerTest {
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDao userDao;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    @Transactional
    public void newPull() throws Exception {
        final String newPullJson = FileUtils.readFileToString(
                new File("src/test/resources/travis_payload.json"), "UTF-8");

        mockMvc.perform(post("/travisci")
                .accept(contentType)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newPullJson))
                .andExpect(status().isOk());

        final Optional<User> userOptional = userDao.findByEmail("daniel.buch@gmail.com");
        assertThat(userOptional).isPresent();

        assertThat(jdbcTemplate.queryForObject(
                "Select COUNT(*) FROM  tasks WHERE user_id = ?",
                new Object[]{userOptional.get().getId()},
                Integer.class
        )).isEqualTo(1);
    }

}