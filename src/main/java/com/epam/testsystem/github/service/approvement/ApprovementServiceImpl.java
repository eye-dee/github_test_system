package com.epam.testsystem.github.service.approvement;

import com.epam.testsystem.github.dao.ApprovementDao;
import com.epam.testsystem.github.enums.ApprovementStatus;
import com.epam.testsystem.github.model.Approvement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApprovementServiceImpl implements ApprovementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApprovementService.class);
    private final ApprovementDao approvementDao;

    @Override
    public Approvement add(final long userId, final long taskId) {
        LOGGER.info("Approvement add from {}, task {}", userId, taskId);
        return approvementDao.add(userId, taskId);
    }

    @Override
    public Optional<Approvement> find(final long userId, final long taskId) {
        return approvementDao.find(userId, taskId);
    }

    @Override
    public boolean markGood(final long userId, final long taskId) {
        LOGGER.info("Approvement from {}, task {}, marked good", userId, taskId);
        return approvementDao.update(userId, taskId, ApprovementStatus.GOOD) != null;
    }

    @Override
    public boolean markBad(final long userId, final long taskId) {
        LOGGER.info("Approvement from {}, task {}, marked bad", userId, taskId);
        return approvementDao.update(userId, taskId, ApprovementStatus.BAD) != null;
    }

    @Override
    public List<Approvement> find(final long taskId) {
        return approvementDao.find(taskId);
    }
}
