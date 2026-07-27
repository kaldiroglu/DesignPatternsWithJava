package dev.kaldiroglu.dp.structural.facade.notification.problem;

/**
 * Subsystem: sends Slack messages via incoming webhook.
 */
public class SlackService {
    private final String webhookUrl;

    public SlackService(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public boolean send(String channel, String body) {
        // In production: HTTP POST to webhookUrl with JSON payload
        System.out.printf("[SLACK] Channel: %s | Body: %s%n", channel, body);
        return true;
    }
}
