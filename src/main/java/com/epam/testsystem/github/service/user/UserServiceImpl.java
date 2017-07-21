package com.epam.testsystem.github.service.user;

import com.epam.testsystem.github.dao.UserDao;
import com.epam.testsystem.github.enums.UserRoleType;
import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.model.UserWithTasks;
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

import java.util.List;
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
    public User register(final String email, final String gitNick, final String password, final UserRoleType roleType) {
        LOGGER.info("register new user {} with nick {}", email, gitNick);
        final String encodedPassword = passwordEncoder.encode(password);

        return userDao.add(email, gitNick, encodedPassword, roleType.name());
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public Optional<User> findById(final long userId) {
        return userDao.findById(userId);
    }

    @Override
    public List<UserWithTasks> findAllWithTasks() {
        return userDao.findAllWithTasks();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
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
