package com.epam.testsystem.github.service.user;

import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.model.UserWithTasks;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

/**
 * github_test
 * Create on 7/11/2017.
 */

public interface UserService extends UserDetailsService {

    User register(String email, String gitNick, String password);

    Optional<User> findByEmail(String email);

    User getCurrentUser();

    Optional<User> findById(long userId);

    List<UserWithTasks> findAllWithTasks();
}
