package com.epam.testsystem.github.web.mapper;

import com.epam.testsystem.github.TimeConstant;
import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.model.UserWithTasks;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * github_test
 * Created on 07.07.17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    private static Task buildSomeTask() {
        return Task.builder().id(1).userId(2)
                .registerTime(LocalDateTime.now())
                .build();
    }

    private static UserWithTasks buildSomeUserWithTasks() {
        final User user = User.builder().id(1).email("email").githubNick("githubNick").build();
        final Task task = buildSomeTask();
        return UserWithTasks.builder().user(user).tasks(Collections.singletonList(task)).build();
    }

    @Test
    public void mapTask() throws Exception {
        final Task task = buildSomeTask();

        assertThat(userMapper.mapTask(task))
                .satisfies(
                        t -> {
                            assertThat(t.getStartTime()).isEqualTo(task.getRegisterTime().format(TimeConstant.DATE_TIME_FORMATTER));
                        }
                );
    }

    @Test
    public void mapUser() throws Exception {
        final UserWithTasks userWithTasks = buildSomeUserWithTasks();
        final User user = userWithTasks.getUser();

        assertThat(userMapper.mapUser(userWithTasks))
                .satisfies(
                        userUI -> {
                            assertThat(userUI.getEmail()).isEqualTo(user.getEmail());
                            assertThat(userUI.getGithubNick()).isEqualTo(user.getGithubNick());
                            assertThat(userUI.getTasks()).hasSize(1);
                        }
                );
    }

    @Test
    public void mapUsers() {
        final List<UserWithTasks> userWithTasksList = Arrays.asList(buildSomeUserWithTasks(), buildSomeUserWithTasks());

        assertThat(userMapper.mapUsers(userWithTasksList))
                .hasSize(2);
    }
}