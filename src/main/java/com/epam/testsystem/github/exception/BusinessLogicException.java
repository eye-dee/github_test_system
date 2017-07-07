package com.epam.testsystem.github.exception;

/**
 * @author <a href="mailto:Daria_Makarova@epam.com">Daria Makarova</a>
 * @version 0.1
 * @since 0.1
 */
public class BusinessLogicException extends ServerException {
    public BusinessLogicException(String message) {
        super(message);
    }

    public BusinessLogicException(String message, Throwable e) {
        super(message, e);
    }
}
