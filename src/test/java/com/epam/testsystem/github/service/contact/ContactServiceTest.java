package com.epam.testsystem.github.service.contact;

import com.epam.testsystem.github.dao.ContactDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
public class ContactServiceTest {
    private static final long ANY_ID = 1;
    private static final long ANY_USER_ID = 1;
    private static final String ANY_TYPE = "VK";
    private static final String ANY_INF = "INF";

    @Autowired
    private ContactService contactService;

    @MockBean
    private ContactDao contactDao;

    @Test
    public void add() throws Exception {
        contactService.add(ANY_USER_ID, ANY_TYPE, ANY_INF);

        verify(contactDao, times(1)).add(eq(ANY_USER_ID), eq(ANY_TYPE), eq(ANY_INF));
    }

    @Test
    public void findByUserId() throws Exception {
        contactService.findByUserId(ANY_USER_ID);

        verify(contactDao, times(1)).findByUserId(eq(ANY_USER_ID));
    }

    @Test
    public void findByUserIdType() throws Exception {
        contactService.findByUserIdType(ANY_USER_ID, ANY_TYPE);

        verify(contactDao, times(1)).findByUserIdType(ANY_USER_ID, ANY_TYPE);
    }

    @Test
    public void findById() throws Exception {
        contactService.findById(ANY_ID);

        verify(contactDao, times(1)).findById(ANY_ID);
    }

    @Test
    public void enableContact() throws Exception {
        contactService.enableContact(ANY_ID);

        verify(contactDao, times(1)).enableContact(ANY_ID);
    }

    @Test
    public void disableContact() throws Exception {
        contactService.disableContact(ANY_ID);

        verify(contactDao, times(1)).disableContact(ANY_ID);
    }

    @Test
    public void updateContact() throws Exception {
        contactService.updateContact(ANY_ID, ANY_INF);

        verify(contactDao, times(1)).updateContact(ANY_ID, ANY_INF);
    }
}