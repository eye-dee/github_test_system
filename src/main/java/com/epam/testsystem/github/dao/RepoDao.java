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
                    .gitNick(rs.getString("repos.git_nick"))
                    .build();

    private final JdbcTemplate jdbcTemplate;

    public Repo add(final long id, final String name, final String gitNick) {
        jdbcTemplate.update(
                "INSERT INTO repos(id, name, git_nick) VALUES(?, ?, ?)",
                id, name, gitNick);

        return singleResult(jdbcTemplate.query(
                "SELECT * FROM repos WHERE id = ?",
                new Object[]{id},
                REPO_ROW_MAPPER)
        );
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

    public List<Repo> findByOwner(final String gitNick) {
        return jdbcTemplate.query(
                "SELECT * FROM repos WHERE git_nick = ?",
                new Object[]{gitNick},
                REPO_ROW_MAPPER
        );
    }

}
