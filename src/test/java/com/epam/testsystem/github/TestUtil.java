package com.epam.testsystem.github;

import com.epam.testsystem.github.dao.TaskDao;
import com.epam.testsystem.github.dao.UserDao;
import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;

/**
 * github_test
 * Created on 05.07.17.
 */

@Service
@Profile(value = SPRING_PROFILE_TEST)
@RequiredArgsConstructor
public class TestUtil {
    private final UserDao userDao;
    private final TaskDao taskDao;
    private long defaultPullId;
    private static SecureRandom random = new SecureRandom();

    public User makeUser() {
        final String email = generateString();
        userDao.add(email, generateString());
        return userDao.findByEmail(email).get();
    }

    public User makeUser(final String email, final String githubNick) {
        userDao.add(email, githubNick);
        return userDao.findByEmail(email).get();
    }

    public Task addTask(final long userId) {
        return taskDao.addOrUpdate(userId, defaultPullId++, false, "log");
    }

    public Task addTask(final long userId, final long pullId, final boolean successful, final String log) {
        return taskDao.addOrUpdate(userId, pullId, successful, log);
    }

    private static String generateString() {
        return new BigInteger(130, random).toString(32);
    }
}
