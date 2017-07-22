package com.epam.testsystem.github.service.contact;

import com.epam.testsystem.github.model.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactService {
    Contact add(long userId, final String type, String inf);

    List<Contact> findByUserId(long userId);

    List<Contact> findByUserIdType(long userId, String type);

    Optional<Contact> findById(long id);

    boolean enable(long id);

    boolean disable(long id);

    boolean update(long id, String inf);
}
