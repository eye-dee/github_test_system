package com.epam.testsystem.github.service.notification.mail;

import com.epam.testsystem.github.enums.EmailTemplateType;
import com.epam.testsystem.github.util.MailInfo;

/**
 * github_test
 * Created on 09.07.17.
 */
public interface MailService {

    void sendMessage(final String emailTo, final String CC, final String subject, final EmailTemplateType emailTemplateType, final MailInfo mailInfo);

}
