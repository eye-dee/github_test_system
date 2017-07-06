package com.epam.testsystem.github.web.mapper;

import com.epam.testsystem.github.TimeConstant;
import com.epam.testsystem.github.model.Task;
import com.epam.testsystem.github.model.UserWithTasks;
import com.epam.testsystem.github.web.model.TaskUI;
import com.epam.testsystem.github.web.model.UserUI;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * github_test
 * Created on 07.07.17.
 */

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "startTime", source = "registerTime", dateFormat = TimeConstant.FORMAT)
    TaskUI mapTask(Task task);

    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "githubNick",source = "user.githubNick")
    UserUI mapUser(UserWithTasks userWithTasks);

    List<UserUI> mapUsers(List<UserWithTasks> userWithTasksList);
}
