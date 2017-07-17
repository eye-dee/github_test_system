package com.epam.testsystem.github.config;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * github_test
 * Create on 7/14/2017.
 */

@Configuration
public class VkConfig {

    @Value("${vk.group.id}")
    private Integer groupId;
    @Value("${vk.group.access_token}")
    private String accessToken;

    @Bean
    public TransportClient transportClient() {
        return HttpTransportClient.getInstance();
    }

    @Bean
    public VkApiClient vkApiClient() {
        return new VkApiClient(transportClient());
    }

    @Bean
    public GroupActor groupActor() {
        return new GroupActor(groupId, accessToken);
    }
}
