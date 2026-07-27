package dev.kaldiroglu.dp.structural.facade.slf4j;

/**
 * Subsystem backend that prints to standard output for any level at or above a
 * threshold. Usable on its own, without the {@link Logger} facade.
 */
public final class ConsoleLoggingEngine implements LoggingEngine {

    private final LogLevel threshold;

    public ConsoleLoggingEngine(LogLevel threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean isEnabled(String loggerName, LogLevel level) {
        return level.isAtLeast(threshold);
    }

    @Override
    public void log(LogEvent event) {
        System.out.printf("[%-5s] %s - %s%n", event.level(), event.loggerName(), event.message());
    }
}
