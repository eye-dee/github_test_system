package com.epam.testsystem.github.service.contact;

import com.epam.testsystem.github.dao.ContactDao;
import com.epam.testsystem.github.model.Contact;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactService.class);
    private final ContactDao contactDao;

    @Transactional
    @Override
    public Contact add(final long userId,
                       final String type,
                       final String inf) {
        LOGGER.info("Contact added of userId {}, type {}, inf {}",
                userId, type, inf);
        return contactDao.add(userId, type, inf);
    }

    @Transactional
    @Override
    public List<Contact> findByUserId(final long userId) {
        return contactDao.findByUserId(userId);
    }

    @Transactional
    @Override
    public List<Contact> findByUserIdType(final long userId, final String type) {
        return contactDao.findByUserIdType(userId, type);
    }

    @Transactional
    @Override
    public Optional<Contact> findById(final long id) {
        return contactDao.findById(id);
    }

    @Transactional
    @Override
    public boolean enable(final long id) {
        LOGGER.info("Contact {} enabled", id);
        return contactDao.enable(id);
    }

    @Transactional
    @Override
    public boolean disable(final long id) {
        LOGGER.info("Contact {} disabled", id);
        return contactDao.disable(id);
    }

    @Transactional
    @Override
    public boolean update(final long id, final String inf) {
        LOGGER.info("Contact {} updated with {}", id, inf);
        return contactDao.update(id, inf);
    }
}
