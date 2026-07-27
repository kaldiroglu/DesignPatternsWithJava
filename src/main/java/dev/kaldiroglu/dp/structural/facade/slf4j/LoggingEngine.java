package dev.kaldiroglu.dp.structural.facade.slf4j;

/**
 * The logging <b>subsystem</b> seam. A backend (console, {@code java.util.logging},
 * a test recorder, ...) implements this interface; the {@link Logger} facade talks
 * only to this type and never to a concrete backend. In real SLF4J this role is
 * played by the bound logging framework behind
 * {@code org.slf4j.spi.SLF4JServiceProvider}.
 */
public interface LoggingEngine {

    /** Whether this backend would record {@code level} for the given logger. */
    boolean isEnabled(String loggerName, LogLevel level);

    /** Writes an already-formatted event to the backend's destination. */
    void log(LogEvent event);
}
