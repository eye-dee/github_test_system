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
                    .owner_id(rs.getLong("repos.owner_id"))
                    .build();

    private final JdbcTemplate jdbcTemplate;

    public Repo add(final long id, final String name, final long owner_id) {
        jdbcTemplate.update(
                "INSERT INTO repos(id, name, owner_id) VALUES(?, ?, ?)",
                id, name, owner_id);

        return Repo.builder()
                .id(id)
                .name(name)
                .owner_id(owner_id)
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

    public List<Repo> findByOwner(final long owner_id) {
        return jdbcTemplate.query(
                "SELECT * FROM repos WHERE owner_id = ?",
                new Object[]{owner_id},
                REPO_ROW_MAPPER
        );
    }

}
