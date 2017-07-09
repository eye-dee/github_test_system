package com.epam.testsystem.github.service;

/**
 * @author <a href="mailto:Daria_Makarova@epam.com">Daria Makarova</a>
 * @version 0.1
 * @since 0.1
 */

public interface MailService {

    void sendGitHubTasksRepositoryLink(String emailTo, String CC,
                                       String subject, String messageText);
}
