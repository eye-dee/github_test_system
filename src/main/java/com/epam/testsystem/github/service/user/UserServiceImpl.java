package com.epam.testsystem.github.service.user;

import com.epam.testsystem.github.dao.UserDao;
import com.epam.testsystem.github.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * github_test
 * Create on 7/11/2017.
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserDao userDao;

    @Override
    public User register(String email, String githubNick, String password) {
        String encodedPassword = passwordEncoder.encode(password);

        return userDao.add(email, githubNick, encodedPassword);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final Optional<User> userOpt = userDao.findByEmail(email);
        if (userOpt.isPresent()) {
            return userOpt.get();
        } else {
            final String message = String.format("User with email: %s not found.", email);
            LOGGER.warn(message);
            throw new UsernameNotFoundException(message);
        }
    }
}
