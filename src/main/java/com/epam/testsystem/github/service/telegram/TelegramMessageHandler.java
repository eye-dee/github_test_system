package com.epam.testsystem.github.service.telegram;

import com.epam.testsystem.github.dao.UserDao;
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

    private SendMessage prepareMessage(Object chatId) {
        return new SendMessage(chatId, "Your tasks")
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            final Message message = update.message();

            if (message != null) {
                if (message.text().equals("/tasks")) {
                    final SendMessage request = prepareMessage(message.chat().id());
                    SendResponse sendResponse = telegramBot.execute(request);
                    boolean ok = sendResponse.isOk();
                    Message m = sendResponse.message();
                }
            }
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
