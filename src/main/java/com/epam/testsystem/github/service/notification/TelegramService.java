package com.epam.testsystem.github.service.notification;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.GetUpdates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * github_test
 * Create on 7/18/2017.
 */

@Service
@RequiredArgsConstructor
public class TelegramService {

    private final TelegramBot telegramBot;

    public void handle() {
        GetUpdates getUpdates = new GetUpdates().limit(100).offset(0).timeout(0);

        // sync
        /*GetUpdatesResponse updatesResponse = telegramBot.execute(getUpdates);
        List<Update> updates = updatesResponse.updates();

        updates.get(0).message()

        Message message = update.message()


        bot.execute(getUpdates, new Callback<GetUpdates, GetUpdatesResponse>() {
            @Override
            public void onResponse(GetUpdates request, GetUpdatesResponse response) {
                List<Update> updates = updatesResponse.updates();
            }

            @Override
            public void onFailure(GetUpdates request, IOException e) {

            }
        });*/
    }
}
