package com.epam.testsystem.github.service.user;

import com.epam.testsystem.github.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * github_test
 * Create on 7/11/2017.
 */

public interface UserService extends UserDetailsService {

    User register(String email, String githubNick, String password);

    Optional<User> findByEmail(String email);

    User getCurrentUser();


}
