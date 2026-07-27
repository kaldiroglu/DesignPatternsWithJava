package dev.kaldiroglu.dp.structural.facade.slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Subsystem backend that keeps events in memory instead of writing them out.
 * Handy for tests, and for showing that a client's logging code is unchanged when
 * the backend is swapped. Usable directly, without the {@link Logger} facade.
 */
public final class RecordingLoggingEngine implements LoggingEngine {

    private final LogLevel threshold;
    private final List<LogEvent> events = new ArrayList<>();

    public RecordingLoggingEngine(LogLevel threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean isEnabled(String loggerName, LogLevel level) {
        return level.isAtLeast(threshold);
    }

    @Override
    public void log(LogEvent event) {
        events.add(event);
    }

    /** An immutable snapshot of everything recorded so far. */
    public List<LogEvent> events() {
        return List.copyOf(events);
    }
}
