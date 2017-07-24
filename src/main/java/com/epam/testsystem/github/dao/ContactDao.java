package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.model.Contact;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.epam.testsystem.github.dao.DaoExtractorUtil.CONTACT_ROW_MAPPER;
import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
@RequiredArgsConstructor
public class ContactDao {
    private final JdbcTemplate jdbcTemplate;

    public Contact add(final long userId,
                       final String type,
                       final String inf) {
        jdbcTemplate.update(
                "INSERT INTO contacts(user_id, type, inf) " +
                        "VALUE (?, ?, ?)",
                userId, type, inf);


        return jdbcTemplate.queryForObject(
                "SELECT * FROM contacts WHERE id = last_insert_id()",
                CONTACT_ROW_MAPPER
        );
    }

    public List<Contact> findByUserId(final long userId) {
        return jdbcTemplate.query(
                "SELECT * FROM contacts WHERE user_id = ?",
                new Object[]{userId},
                CONTACT_ROW_MAPPER
        );
    }


    public List<Contact> findByUserIdType(final long userId, final String type) {
        return jdbcTemplate.query(
                "SELECT * FROM contacts WHERE user_id = ? AND type = ?",
                new Object[]{userId, type},
                CONTACT_ROW_MAPPER
        );
    }

    public Optional<Contact> findById(final long id) {
        return Optional.ofNullable(
                singleResult(jdbcTemplate.query(
                        "SELECT * FROM contacts WHERE id = ?",
                        new Object[]{id},
                        CONTACT_ROW_MAPPER))
        );
    }

    public boolean enable(final long id) {
        return jdbcTemplate.update(
                "UPDATE contacts SET enabled = TRUE WHERE id = ?",
                id
        ) > 0;
    }

    public boolean disable(final long id) {
        return jdbcTemplate.update(
                "UPDATE contacts SET enabled = FALSE WHERE id = ?",
                id
        ) > 0;
    }

    public boolean update(final long id, final String inf) {
        return jdbcTemplate.update(
                "UPDATE contacts SET inf = ? WHERE id = ?",
                inf, id
        ) > 0;
    }
}
