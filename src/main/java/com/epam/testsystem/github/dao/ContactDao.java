package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.model.Contact;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
@RequiredArgsConstructor
public class ContactDao {
    private static final RowMapper<Contact> CONTACT_ROW_MAPPER = (rs, rowNum) ->
            Contact.builder()
                    .id(rs.getLong("contacts.id"))
                    .userId(rs.getLong("contacts.user_id"))
                    .type(rs.getString("contacts.type"))
                    .inf(rs.getString("contacts.inf"))
                    .enabled(rs.getBoolean("contacts.enabled"))
                    .build();

    private final JdbcTemplate jdbcTemplate;

    public Contact add(final long userId,
                       final String type,
                       final String inf,
                       final boolean enabled) {
        jdbcTemplate.update(
                "INSERT INTO contacts(user_id, type, inf, enabled) " +
                        "VALUE (?, ?, ?, ?)",
                userId, type, inf, enabled);

        final Integer id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

        return Contact.builder()
                .id(id)
                .userId(userId)
                .type(type)
                .inf(inf)
                .enabled(enabled)
                .build();
    }

    public List<Contact> findByUserId(final long userId) {
        return jdbcTemplate.query(
                "SELECT * FROM contacts WHERE user_id = ?",
                new Object[]{userId},
                CONTACT_ROW_MAPPER
        );
    }

    public List<Long> findUserIdByInf(final String type, final String inf) {
        return jdbcTemplate.queryForList(
                "SELECT user_id FROM contacts WHERE type = ? AND inf = ?",
                new Object[]{type, inf},
                Long.class
        );
    }
}
