package com.epam.testsystem.github.service.repo;

import com.epam.testsystem.github.model.Repo;

public interface RepoService {
    Repo add(long user_id, String name, long repo_id);
}
