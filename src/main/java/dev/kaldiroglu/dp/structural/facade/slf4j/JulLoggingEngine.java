package dev.kaldiroglu.dp.structural.facade.slf4j;

import java.util.logging.Level;

/**
 * Subsystem backend that forwards to the JDK's built-in {@code java.util.logging}
 * framework -- a real, independently-usable logging system living behind the
 * facade. This mirrors what the real {@code slf4j-jdk14} binding does.
 */
public final class JulLoggingEngine implements LoggingEngine {

    @Override
    public boolean isEnabled(String loggerName, LogLevel level) {
        return java.util.logging.Logger.getLogger(loggerName).isLoggable(toJulLevel(level));
    }

    @Override
    public void log(LogEvent event) {
        java.util.logging.Logger.getLogger(event.loggerName()).log(toJulLevel(event.level()), event.message());
    }

    private static Level toJulLevel(LogLevel level) {
        return switch (level) {
            case TRACE -> Level.FINER;
            case DEBUG -> Level.FINE;
            case INFO -> Level.INFO;
            case WARN -> Level.WARNING;
            case ERROR -> Level.SEVERE;
        };
    }
}
