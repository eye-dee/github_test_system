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
    public Repo add(final long repoId, final String name, final String gitNick) {
        LOGGER.info("Add repo {}, from user {}", name, gitNick);
        return repoDao.add(repoId, name, gitNick);
    }
}
