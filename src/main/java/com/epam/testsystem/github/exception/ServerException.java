package com.epam.testsystem.github.exception;

/**
 * @author <a href="mailto:Daria_Makarova@epam.com">Daria Makarova</a>
 * @version 0.1
 * @since 0.1
 */
public class ServerException extends RuntimeException {
    public ServerException(final String message) {
        super(message);
    }

    public ServerException(final String message, final Throwable e) {
        super(message, e);
    }

}
