package com.epam.testsystem.github.enums;

/**
 * @author <a href="mailto:Daria_Makarova@epam.com">Daria Makarova</a>
 * @version 0.1
 * @since 0.1
 */
public enum EmailTemplateType {
    REPOSITORY_LINK("email-repository-link.ftl"),
    SOLUTION_RECEIVING_CONFIRMATION("email-solution-receiving-confirmation.ftl"),
    SOLUTION_RECEIVING_CONFIRMATION_WITHOUT_REGISTRATION("email-solution-receiving-confirmation-without-registration.ftl"),
    REGISTRATION_CONFIRMATION("email-registration-confirmation.ftl");

    private final String emailTemplateTypeName;

    EmailTemplateType(String emailTemplateTypeName) {
        this.emailTemplateTypeName = emailTemplateTypeName;
    }

    public String getEmailTemplateByName() {
        return emailTemplateTypeName;
    }
}
