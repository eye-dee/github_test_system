package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.TaskStatus;
import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.model.UserWithTasks;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

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
    static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) ->
            User.builder()
                    .id(rs.getLong("users.id"))
                    .email(rs.getString("users.email"))
                    .githubNick(rs.getString("users.github_nick"))
                    .build();
    static final RowMapper<Task> TASK_ROW_MAPPER = (rs, rowNum) ->
            Task.builder()
                    .id(rs.getLong("tasks.id"))
                    .userId(rs.getLong("tasks.user_id"))
                    .registerTime(rs.getTimestamp("tasks.register_time").toLocalDateTime())
                    .status(TaskStatus.valueOf(rs.getString("tasks.status")))
                    .successful(rs.getBoolean("tasks.successful"))
                    .build();

    @Override
    public List<UserWithTasks> extractData(final ResultSet rs) throws SQLException, DataAccessException {
        Map<User,List<Task>> userListMap = new HashMap<>();
        while (rs.next()) {
            final User user = USER_ROW_MAPPER.mapRow(rs, rs.getRow());
            final Task task = TASK_ROW_MAPPER.mapRow(rs, rs.getRow());

            final List<Task> userTasks = userListMap.get(user);
            if (userTasks == null) {
                final List<Task> tasks = new ArrayList<>();
                tasks.add(task);
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
