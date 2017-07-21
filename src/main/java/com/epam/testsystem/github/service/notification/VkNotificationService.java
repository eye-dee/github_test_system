package com.epam.testsystem.github.service.notification;

import com.epam.testsystem.github.exception.BusinessLogicException;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * github_test
 * Create on 7/14/2017.
 */

@Service
@RequiredArgsConstructor
public class VkNotificationService implements NotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VkNotificationService.class);

    private final VkApiClient vkApiClient;
    private final GroupActor groupActor;

    @Override
    public void sendNotification(final String userId, final String message) {
        try {
            final int value = Integer.parseInt(userId);
            LOGGER.info("send message for user {}", value);
            vkApiClient.messages().send(groupActor)
                    .userId(value)
                    .message(message)
                    .execute();
        } catch (ApiException | ClientException e) {
            LOGGER.error("Can't execute request\nWith error {}", e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
    }
}
