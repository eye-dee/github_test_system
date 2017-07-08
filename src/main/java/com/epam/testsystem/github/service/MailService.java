package com.epam.testsystem.github.service;

/**
 * github_test
 * Created on 09.07.17.
 */
public interface MailService {
    void sendMessage(
            String emailTo, String CC, String subject, String messageText
    );

}
