package com.epam.testsystem.github.service.repo;

import com.epam.testsystem.github.model.Repo;

public interface RepoService {
    Repo add(final long repoId, String name, String gitNick);
}
