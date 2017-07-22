package com.epam.testsystem.github.web.controller;

import com.epam.testsystem.github.TestUtil;
import com.epam.testsystem.github.dao.ContactDao;
import com.epam.testsystem.github.model.Contact;
import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.web.model.NewContactUI;
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
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles(SPRING_PROFILE_TEST)
public class NotificationRestControllerTest {
    final private static String TYPE = "VK";
    final private static String INF = "inf";

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TestUtil testUtil;

    private MockMvc mockMvc;

    @Autowired
    private ContactDao contactDao;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    @Transactional
    public void getAllContacts() throws Exception {
        final User user = testUtil.getMainUser();

        final Contact contact = contactDao.add(user.getId(), TYPE, INF);

        mockMvc.perform(get("/notifications")
                .accept(contentType)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is((int) contact.getId())))
                .andExpect(jsonPath("$[0].userId", is((int) contact.getUserId())))
                .andExpect(jsonPath("$[0].type", is(contact.getType())))
                .andExpect(jsonPath("$[0].inf", is(contact.getInf())))
                .andExpect(jsonPath("$[0].enabled", is(contact.isEnabled())));
    }

    @Test
    @Transactional
    public void add() throws Exception {
        final User user = testUtil.getMainUser();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String newContactJson = objectMapper.writeValueAsString(
                NewContactUI.builder()
                        .userId(user.getId())
                        .inf(INF)
                        .type(TYPE)
                        .build()
        );

        mockMvc.perform(post("/notifications/")
                .accept(contentType)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newContactJson))
                .andExpect(status().isOk());

        assertThat(contactDao.findByUserId(user.getId())).hasSize(1);
    }

    @Test
    @Transactional
    public void updateContact() throws Exception {
        final User user = testUtil.getMainUser();
        final Contact contact = contactDao.add(user.getId(), TYPE, INF);
        final String modifiedInf = "new_inf";

        final ObjectMapper objectMapper = new ObjectMapper();
        final String modifiedContact = objectMapper.writeValueAsString(
                NewContactUI.builder()
                        .userId(contact.getUserId())
                        .inf(modifiedInf)
                        .type(contact.getType())
                        .build()
        );

        mockMvc.perform(put("/notifications/" + contact.getId() + "/update")
                .accept(contentType)
                .contentType(MediaType.APPLICATION_JSON)
                .content(modifiedContact))
                .andExpect(status().isOk());

        assertThat(contactDao.findById(contact.getId()))
                .hasValueSatisfying(c -> assertThat(c.getInf()).isEqualTo(modifiedInf));
    }

    @Test
    @Transactional
    public void toggle() throws Exception {
        final User user = testUtil.getMainUser();
        final Contact contact = contactDao.add(user.getId(), TYPE, INF);

        mockMvc.perform(put("/notifications/" + contact.getId() + "?enable=false")
                .accept(contentType)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        assertThat(contactDao.findById(contact.getId()))
                .hasValueSatisfying(c -> assertThat(c.isEnabled()).isFalse());
    }

}