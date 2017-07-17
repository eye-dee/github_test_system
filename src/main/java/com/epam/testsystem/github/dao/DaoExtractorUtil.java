package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.model.GradleLog;
import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.model.UserWithTasks;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * github_test
 * Created on 06.07.17.
 */

@Service
public class DaoExtractorUtil implements ResultSetExtractor<List<UserWithTasks>> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static GradleLog getGradleLog(String log) {
        try {
            return OBJECT_MAPPER.readValue(log, GradleLog.class);
        } catch (IOException e) {
            return new GradleLog();
        }
    }

    static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) ->
            User.builder()
                    .id(rs.getLong("users.id"))
                    .email(rs.getString("users.email"))
                    .githubNick(rs.getString("users.github_nick"))
                    .password(rs.getString("users.password"))
                    .build();
    static final RowMapper<Task> TASK_ROW_MAPPER = (rs, rowNum) ->
            Task.builder()
                    .id(rs.getLong("tasks.id"))
                    .userId(rs.getLong("tasks.user_id"))
                    .repoId(rs.getLong("tasks.repo_id"))
                    .registerTime(rs.getTimestamp("tasks.register_time").toLocalDateTime())
                    .successful(rs.getBoolean("tasks.successful"))
                    .log(rs.getString("tasks.log"))
                    .build();

    @Override
    public List<UserWithTasks> extractData(final ResultSet rs) throws SQLException, DataAccessException {
        final Map<User, List<Task>> userListMap = new HashMap<>();
        while (rs.next()) {
            final User user = USER_ROW_MAPPER.mapRow(rs, rs.getRow());

            final Task task;
            if (rs.getTimestamp("tasks.register_time") != null) {
                task = TASK_ROW_MAPPER.mapRow(rs, rs.getRow());
            } else {
                task = null;
            }

            final List<Task> userTasks = userListMap.get(user);
            if (userTasks == null) {
                final List<Task> tasks = new ArrayList<>();
                if (task != null) {
                    tasks.add(task);
                }
                userListMap.put(user, tasks);
            } else {
                userTasks.add(task);
            }
        }

        return userListMap.entrySet().stream()
                .map(entry -> UserWithTasks.builder().user(entry.getKey()).tasks(entry.getValue()).build())
                .collect(Collectors.toList());
    }
}
