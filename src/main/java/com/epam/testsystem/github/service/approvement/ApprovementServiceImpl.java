package com.epam.testsystem.github.service.approvement;

import com.epam.testsystem.github.dao.ApprovementDao;
import com.epam.testsystem.github.enums.ApprovementStatus;
import com.epam.testsystem.github.model.Approvement;
import com.epam.testsystem.github.model.ApprovementUserTask;
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
    public Optional<ApprovementUserTask> find(final long userId, final long taskId) {
        return approvementDao.find(userId, taskId);
    }

    @Override
    public boolean markAs(final long userId, final long taskId, final ApprovementStatus mark, final String comment) {
        LOGGER.info("Approvement from {}, task {}, marked {}", userId, taskId, mark.name());
        return approvementDao.update(userId, taskId, mark, comment) != null;
    }

    @Override
    public List<ApprovementUserTask> find(final long taskId) {
        return approvementDao.find(taskId);
    }
}
