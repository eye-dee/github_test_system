package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.model.Repo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.dao.support.DataAccessUtils.singleResult;

/**
 * github_test
 * Created on 13.07.17.
 */

@Repository
@Transactional(propagation = Propagation.MANDATORY)
@RequiredArgsConstructor
public class RepoDao {
    private static final RowMapper<Repo> REPO_ROW_MAPPER = (rs, rowNum) ->
            Repo.builder()
                    .id(rs.getLong("repos.id"))
                    .name(rs.getString("repos.name"))
                    .owner(rs.getString("repos.owner"))
                    .build();

    private final JdbcTemplate jdbcTemplate;

    public Repo add(final String name, final String owner) {
        jdbcTemplate.update(
                "INSERT INTO repos(name, owner) VALUES(?, ?)",
                name, owner);

        final Integer id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

        return Repo.builder()
                .id(id)
                .name(name)
                .owner(owner)
                .build();
    }

    public Optional<Repo> findById(final long id) {
        return Optional.ofNullable(
                singleResult(jdbcTemplate.query(
                        "SELECT * FROM repos WHERE id = ?",
                        new Object[]{id},
                        REPO_ROW_MAPPER
                        )
                ));
    }

    public List<Repo> findByOwner(final String owner) {
        return jdbcTemplate.query(
                "SELECT * FROM repos WHERE owner = ?",
                new Object[]{owner},
                REPO_ROW_MAPPER
        );
    }

}
