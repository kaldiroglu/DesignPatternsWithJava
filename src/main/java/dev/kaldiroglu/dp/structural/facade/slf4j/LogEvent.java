package dev.kaldiroglu.dp.structural.facade.slf4j;

/**
 * A single log record handed from the facade to the logging subsystem: which
 * logger produced it, at what level, and the already-formatted message.
 */
public record LogEvent(String loggerName, LogLevel level, String message) {
}
