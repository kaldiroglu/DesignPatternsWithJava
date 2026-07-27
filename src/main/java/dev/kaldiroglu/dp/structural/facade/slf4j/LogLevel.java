package dev.kaldiroglu.dp.structural.facade.slf4j;

/**
 * Severity levels, ordered from least to most severe. Mirrors the five levels of
 * real SLF4J: {@code TRACE < DEBUG < INFO < WARN < ERROR}.
 */
public enum LogLevel {

    TRACE(0),
    DEBUG(1),
    INFO(2),
    WARN(3),
    ERROR(4);

    private final int severity;

    LogLevel(int severity) {
        this.severity = severity;
    }

    /** True when this level is at least as severe as {@code threshold}. */
    public boolean isAtLeast(LogLevel threshold) {
        return this.severity >= threshold.severity;
    }
}
