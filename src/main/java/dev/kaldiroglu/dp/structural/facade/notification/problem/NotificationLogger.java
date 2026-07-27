package dev.kaldiroglu.dp.structural.facade.notification.problem;

import java.time.Instant;

/**
 * Subsystem: writes notification audit records.
 */
public class NotificationLogger {
    private final String logFilePath;

    public NotificationLogger(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    public void record(String userId, String channel, boolean success) {
        // In production: structured JSON lines, async write, rotation
        System.out.printf("[LOG]   %s | user=%s channel=%s success=%s%n",
                Instant.now(), userId, channel, success);
    }
}
