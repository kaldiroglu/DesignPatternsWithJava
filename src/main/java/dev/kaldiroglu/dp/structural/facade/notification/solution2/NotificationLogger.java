package dev.kaldiroglu.dp.structural.facade.notification.solution2;

import java.time.Instant;

public class NotificationLogger {
    private final String logFilePath;

    public NotificationLogger(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    public void record(String userId, String channel, boolean success) {
        System.out.printf("[LOG]   %s | user=%s channel=%s success=%s%n",
                Instant.now(), userId, channel, success);
    }
}
