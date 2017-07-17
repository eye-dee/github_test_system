package com.epam.testsystem.github.service.notification;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;

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

    @Test
    public void sendNotification() throws Exception {
        vkNotificationService.sendNotification("59659343", "Hi");
    }

}