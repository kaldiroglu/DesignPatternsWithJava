package dev.kaldiroglu.dp.structural.flyweight.hw.log;

import java.time.Instant;

/**
 * One log line: the part that differs every time, plus a pointer to the part that does not.
 *
 * <p>Three fields of its own. Written the obvious way it would have seven, four of which
 * would repeat a few hundred distinct values across every line the process ever emits.</p>
 */
public record LogEvent(LogSource source, Instant at, String level, String message) {

    public String render() {
        return at + " " + level + " " + source + " - " + message;
    }
}
