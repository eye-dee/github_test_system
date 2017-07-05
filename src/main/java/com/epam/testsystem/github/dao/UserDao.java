package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.dao.support.DataAccessUtils.singleResult;

/**
 * github_test
 * Created on 05.07.17.
 */

@Repository
@Transactional(propagation = Propagation.MANDATORY)
@RequiredArgsConstructor
public class UserDao {
    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) ->
            User.builder()
                    .id(rs.getLong("id"))
                    .email(rs.getString("email"))
                    .githubNick(rs.getString("github_nick"))
                    .build();

    private final JdbcTemplate jdbcTemplate;

    public boolean add(final String email, final String githubNick) {
        return jdbcTemplate.update(
                "INSERT INTO users(email, github_nick) VALUES (?, ?) ",
                email, githubNick) > 0;
    }

    public Optional<User> findByEmail(final String email) {
        return Optional.ofNullable(
                singleResult(jdbcTemplate.query(
                        "SELECT * FROM users WHERE email = ?",
                        new Object[]{email},
                        USER_ROW_MAPPER
                        )
                )
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
}
