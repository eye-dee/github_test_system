package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.TestUtil;
import com.epam.testsystem.github.model.Repo;
import com.epam.testsystem.github.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * github_test
 * Created on 13.07.17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
public class RepoDaoTest {
    @Autowired
    private RepoDao repoDao;

    @Autowired
    private TestUtil testUtil;

    @Test
    @Transactional
    public void add() throws Exception {
        int id = 1;
        final User user = testUtil.getMainUser();
        final String name = "name";

        assertThat(repoDao.add(id, name, user.getId()))
                .satisfies(
                        repo -> {
                            assertThat(repo.getId()).isEqualTo(id);
                            assertThat(repo.getName()).isEqualTo(name);
                            assertThat(repo.getOwner_id()).isEqualTo(user.getId());
                        }
                );
    }

    @Test
    @Transactional
    public void findById() throws Exception {
        final Repo repo = testUtil.addRepo();

        assertThat(repoDao.findById(0)).isEmpty();
        assertThat(repoDao.findById(repo.getId())).contains(repo);
    }

    @Test
    @Transactional
    public void findByOwner() throws Exception {
        final User user = testUtil.getMainUser();

        final Repo repo1 = testUtil.addRepo(TestUtil.generateString(), user.getId());
        final Repo repo2 = testUtil.addRepo(TestUtil.generateString(), user.getId());

        assertThat(repoDao.findByOwner(user.getId()))
                .containsOnlyOnce(repo1, repo2);
    }

}