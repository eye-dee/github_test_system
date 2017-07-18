package com.epam.testsystem.github.web.controller;

import com.epam.testsystem.github.TestUtil;
import com.epam.testsystem.github.dao.UserDao;
import com.epam.testsystem.github.service.notification.mail.MailService;
import com.epam.testsystem.github.web.model.NewUserUI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
 * Create on 7/12/2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles(SPRING_PROFILE_TEST)
public class RegistrationRestControllerTest {
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TestUtil testUtil;

    @MockBean
    private MailService mailService;

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
                        .githubNick("gitNick")
                        .password("password")
                        .build()
        );

        mockMvc.perform(post("/registration/user")
                .accept(contentType)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson))
                .andExpect(status().isOk());

        assertThat(userDao.findByEmail("email"))
                .hasValueSatisfying(
                        user -> {
                            assertThat(user.getId()).isGreaterThan(0);
                            assertThat(user.getEmail()).isEqualTo("email");
                            assertThat(user.getGitNick()).isEqualTo("gitNick");
                            assertThat(user.getPassword()).isNotEmpty();
                        }
                );
    }
}