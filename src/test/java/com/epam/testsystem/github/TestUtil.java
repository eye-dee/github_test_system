package com.epam.testsystem.github;

import com.epam.testsystem.github.dao.RepoDao;
import com.epam.testsystem.github.dao.TaskDao;
import com.epam.testsystem.github.dao.UserDao;
import com.epam.testsystem.github.model.Repo;
import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;

/**
 * github_test
 * Created on 05.07.17.
 */

@Service
@Profile(value = SPRING_PROFILE_TEST)
@RequiredArgsConstructor
public class TestUtil {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final Random RANDOM = new Random();
    private final UserDao userDao;
    private final TaskDao taskDao;
    private final RepoDao repoDao;
    private User mainUser = null;

    private long defaultRepoId;

    public static String generateString() {
        return new BigInteger(130, SECURE_RANDOM).toString(32);
    }

    @Transactional
    public User makeUser() {
        final String email = generateString();
        final User user = userDao.add(email, generateString(), generateString());

        if (mainUser == null) {
            mainUser = user;
        }

        return user;
    }

    public User makeUser(final String email, final String githubNick) {
        userDao.add(email, githubNick, generateString());
        return userDao.findByEmail(email).get();
    }

    public Repo addRepo() {
        return repoDao.add(RANDOM.nextInt(), generateString(), generateString());
    }

    public Repo addRepo(final String name, final String owner) {
        return repoDao.add(RANDOM.nextInt(), name, owner);
    }

    public Task addTask(final long userId) {
        return taskDao.addOrUpdate(userId, ++defaultRepoId, false, "log");
    }

    public Task addTask(final long userId, final long repoId, final boolean successful, final String log) {
        return taskDao.addOrUpdate(userId, repoId, successful, log);
    }

    public User getMainUser() {
        return mainUser;
    }
}
