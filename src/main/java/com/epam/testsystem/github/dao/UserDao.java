package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.model.UserWithTasks;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.epam.testsystem.github.dao.DaoExtractorUtil.USER_ROW_MAPPER;
import static org.springframework.dao.support.DataAccessUtils.singleResult;

/**
 * github_test
 * Created on 05.07.17.
 */

@Repository
@Transactional(propagation = Propagation.MANDATORY)
@RequiredArgsConstructor
public class UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final DaoExtractorUtil daoExtractorUtil;

    public User add(final String email, final String githubNick) {
        jdbcTemplate.update(
                "INSERT INTO users(email, github_nick) VALUES (?, ?) ",
                email, githubNick);

        final Integer id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

        return User.builder()
                .id(id)
                .email(email)
                .githubNick(githubNick)
                .build();
    }

    public Optional<User> findByEmail(final String email) {
        return Optional.ofNullable(
                singleResult(jdbcTemplate.query(
                        "SELECT * FROM users WHERE email = ?",
                        new Object[]{email},
                        USER_ROW_MAPPER))
        );
    }

    public Optional<User> findById(final long id) {
        return Optional.ofNullable(
                singleResult(jdbcTemplate.query(
                        "SELECT * FROM users WHERE id = ?",
                        new Object[]{id},
                        USER_ROW_MAPPER))
        );
    }

    public List<UserWithTasks> findAllWithTasks() {
        return jdbcTemplate.query(
                "SELECT * FROM tasks RIGHT JOIN users ON tasks.user_id = users.id",
                daoExtractorUtil
        );
    }
}
