package com.epam.testsystem.github.config;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * github_test
 * Create on 7/18/2017.
 */

@Configuration
public class TelegramConfig {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Bean
    public TelegramBot telegramBot() {
       return TelegramBotAdapter.build(botToken);
    }
}
