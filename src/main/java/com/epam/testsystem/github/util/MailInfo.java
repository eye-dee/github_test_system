package com.epam.testsystem.github.util;

import lombok.Builder;
import lombok.Data;

/**
 * @author <a href="mailto:Daria_Makarova@epam.com">Daria Makarova</a>
 * @version 0.1
 * @since 0.1
 */
@Data
@Builder
public class MailInfo {
    private String userName;
    private String email;
    private String password;
}

