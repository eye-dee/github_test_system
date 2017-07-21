package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.TestUtil;
import com.epam.testsystem.github.model.Contact;
import com.epam.testsystem.github.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
public class ContactDaoTest {
    private final static String TEST_INF = "user@mail.ru";
    @Autowired
    private ContactDao contactDao;

    @Autowired
    private TestUtil testUtil;

    @Test
    @Transactional
    public void addContact() {
        final User user = testUtil.getMainUser();
        final boolean enabled = true;
        final String type = "VK";

        assertThat(contactDao.add(user.getId(), type, TEST_INF, enabled))
                .satisfies(
                        c -> {
                            assertThat(c.getId()).isGreaterThan(0);
                            assertThat(c.getInf()).isEqualTo(TEST_INF);
                            assertThat(c.getType()).isEqualTo(type);
                            assertThat(c.getUserId()).isEqualTo(user.getId());
                        }
                );
    }

    @Test
    @Transactional
    public void findContactByIdEmpty() {
        assertThat(contactDao.findById(1)).isEmpty();
    }

    @Test
    @Transactional
    public void findContactById() {
        final User user = testUtil.getMainUser();
        final Contact contact = contactDao.add(user.getId(), "TELEGRAM", TEST_INF, false);

        assertThat(contactDao.findById(contact.getId())).contains(contact);
    }

    @Test
    @Transactional
    public void findAllUserContacts() {
        final User user = testUtil.getMainUser();

        contactDao.add(user.getId(), "VK", TEST_INF, true);
        contactDao.add(user.getId(), "MAIL", TEST_INF, true);

        assertThat(contactDao.findByUserId(user.getId())).hasSize(2);
    }

    @Test
    @Transactional
    public void findAllUserContactsWithType() {
        final User user = testUtil.getMainUser();

        contactDao.add(user.getId(), "VK", TEST_INF, true);
        contactDao.add(user.getId(), "TELEGRAM", TEST_INF, true);

        assertThat(contactDao.findByUserIdType(user.getId(), "VK")).hasSize(1);
    }

    @Test
    @Transactional
    public void addRepeatedTelegram() {
        final User user = testUtil.getMainUser();

        contactDao.add(user.getId(), "TELEGRAM", TEST_INF, true);

        assertThatThrownBy(() ->
                contactDao.add(user.getId(), "TELEGRAM", "New", true)
        ).hasCauseExactlyInstanceOf(SQLException.class);
    }

    @Test
    @Transactional
    public void enableContact() {
        final User user = testUtil.getMainUser();
        final Contact contact = contactDao.add(user.getId(), "TELEGRAM", TEST_INF, false);

        contactDao.enableContact(contact.getId());

        assertThat(contactDao.findById(contact.getId()))
                .hasValueSatisfying(c -> assertThat(c.isEnabled()).isTrue());
    }

    @Test
    @Transactional
    public void updateContact() {
        final User user = testUtil.getMainUser();
        final Contact contact = contactDao.add(user.getId(), "TELEGRAM", TEST_INF, false);

        contactDao.updateContact(contact.getId(), "new_value");

        assertThat(contactDao.findById(contact.getId()).get().getInf()).isEqualTo("new_value");
    }
}