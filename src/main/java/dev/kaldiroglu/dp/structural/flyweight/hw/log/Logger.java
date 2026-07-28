package dev.kaldiroglu.dp.structural.flyweight.hw.log;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

/**
 * Collects events, sharing the call-site metadata across all of them.
 *
 * <p>The clock is injected so a test can assert on timestamps without waiting for one.</p>
 */
public class Logger {

    private final LogSourceRegistry registry;
    private final Clock clock;
    private final List<LogEvent> events = new ArrayList<>();

    public Logger(LogSourceRegistry registry, Clock clock) {
        this.registry = registry;
        this.clock = clock;
    }

    public void log(String level, String loggerName, String className, String methodName,
                    String fileName, String message) {
        LogSource source = registry.get(loggerName, className, methodName, fileName);
        events.add(new LogEvent(source, clock.instant(), level, message));
    }

    public List<LogEvent> events() {
        return List.copyOf(events);
    }

    public int eventCount() {
        return events.size();
    }

    public int distinctSources() {
        return registry.size();
    }
}
