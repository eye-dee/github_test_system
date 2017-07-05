package com.epam.testsystem.discriminator;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.AbstractDiscriminator;

public class LoggerNameDiscriminator extends AbstractDiscriminator<ILoggingEvent> {
    @Override
    public String getDiscriminatingValue(final ILoggingEvent iLoggingEvent) {
        final String name = iLoggingEvent.getLoggerName();
        final int lastDot = name.lastIndexOf('.');

        if (lastDot > 0) {
            return name.substring(lastDot + 1);
        } else {
            return name;
        }
    }

    @Override
    public String getKey() {
        return "loggerName";
    }
}
