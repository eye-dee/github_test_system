package com.epam.testsystem.github.service.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * github_test
 * Create on 7/18/2017.
 */

@Service
@RequiredArgsConstructor
public class TelegramMessageHandler implements UpdatesListener {

    private final TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    private SendMessage prepareMessage(Object chatId, String message) {
        return new SendMessage(chatId, message)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            final Message message = update.message();

            if (message != null) {
                final String text = message.text();

                final Long chatId = message.chat().id();
                if (text.equals("/tasks")) {
                    tasksHandler(chatId);
                } else if (text.equals("/start")) {
                    startHandler(chatId);
                }
            }
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void startHandler(Long chatId) {
        sendMessage(chatId, "Hello my friend");
    }

    private void tasksHandler(Long chatId) {
        // TODO: 7/19/2017 get user his tasks 
        sendMessage(chatId, "Your tasks:");
    }

    public void sendMessage(Long chatId, String message) {
        final SendMessage request = prepareMessage(chatId, message);
        SendResponse sendResponse = telegramBot.execute(request);
        boolean ok = sendResponse.isOk();
        Message m = sendResponse.message();
    }
}
