package com.epam.testsystem.github.service.repo;


import com.epam.testsystem.github.dao.RepoDao;
import com.epam.testsystem.github.model.Repo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RepoServiceImpl implements RepoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepoService.class);
    private final RepoDao repoDao;

    @Override
    public Repo add(final long repo_id, final String name, final long owner_id) {
        LOGGER.info("Add repo {}, from user_id {}", name, owner_id);
        return repoDao.add(repo_id, name, owner_id);
    }
}
