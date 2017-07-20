package com.epam.testsystem.github.service.user;

import com.epam.testsystem.github.dao.UserDao;
import com.epam.testsystem.github.enums.UserRoleType;
import com.epam.testsystem.github.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * github_test
 * Create on 7/19/2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
public class UserServiceImplTest {
    private static final long ANY_USER_ID = 1;
    private static final String ANY_EMAIL = "a@a.com";
    private static final String ANY_NICK = "git_nick";
    private static final String ANY_PASSWORD = "password";
    @MockBean
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Test
    public void register() throws Exception {
        final UserDao mock = mock(UserDao.class);
        userService = new UserServiceImpl(mock);
        userService.register(ANY_EMAIL, ANY_NICK, ANY_PASSWORD, UserRoleType.ROLE_USER);

        verify(mock, times(1)).add(eq(ANY_EMAIL), eq(ANY_NICK), anyString(), eq(UserRoleType.ROLE_USER.toString()));
    }

    @Test
    public void findByEmail() throws Exception {
        userService.findByEmail(ANY_EMAIL);

        verify(userDao, times(1)).findByEmail(ANY_EMAIL);
    }

    @Test
    public void findUserWithTasks() {
        userService.findAllWithTasks();

        verify(userDao, times(1)).findAllWithTasks();
    }

    @Test
    public void findById() throws Exception {
        userService.findById(ANY_USER_ID);

        verify(userDao, times(1)).findById(ANY_USER_ID);
    }

    @Test
    public void loadUserByUsername() throws Exception {
        doReturn(Optional.of(User.builder().build())).when(userDao).findByEmail(ANY_EMAIL);

        userService.loadUserByUsername(ANY_EMAIL);

        verify(userDao, times(1)).findByEmail(ANY_EMAIL);
    }

    @Test
    public void loadUserByUsernameRaiseException() {
        doReturn(Optional.empty()).when(userDao).findByEmail(ANY_EMAIL);

        assertThatThrownBy(() -> userService.loadUserByUsername(ANY_EMAIL))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageMatching(".*User with email: " + ANY_EMAIL + " not found..*");
    }

}