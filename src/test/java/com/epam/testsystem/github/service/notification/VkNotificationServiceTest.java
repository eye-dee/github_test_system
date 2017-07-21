package com.epam.testsystem.github.service.notification;

import com.epam.testsystem.github.exception.BusinessLogicException;
import com.vk.api.sdk.actions.Messages;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.mockito.Mockito.*;

/**
 * github_test
 * Create on 7/17/2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
public class VkNotificationServiceTest {

    @Autowired
    private NotificationService vkNotificationService;

    @MockBean
    private VkApiClient vkApiClient;

    @Test
    public void sendNotification() throws Exception {
        final int userId = 59659343;

        final Messages messagesMock = mock(Messages.class);
        final MessagesSendQuery messagesSendQuery = mock(MessagesSendQuery.class);
        doReturn(messagesMock).when(vkApiClient).messages();
        doReturn(messagesSendQuery).when(messagesMock).send(any(GroupActor.class));
        doReturn(messagesSendQuery).when(messagesSendQuery).userId(userId);
        doReturn(messagesSendQuery).when(messagesSendQuery).message(anyString());

        vkNotificationService.sendNotification(String.valueOf(userId), "Hi");
        verify(vkApiClient, times(1)).messages();
    }

    @Test(expected = BusinessLogicException.class)
    public void sendNotificationException() {
        doThrow(new BusinessLogicException("123")).when(vkApiClient).messages();

        vkNotificationService.sendNotification("-1", "Hi");
    }

}