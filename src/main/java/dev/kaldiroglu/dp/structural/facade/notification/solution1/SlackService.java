package dev.kaldiroglu.dp.structural.facade.notification.solution1;

/**
 * Subsystem: sends Slack messages via incoming webhook.
 * Identical to the before version — subsystems are unchanged.
 */
public class SlackService {
    private final String webhookUrl;

    public SlackService(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public boolean send(String channel, String body) {
        System.out.printf("[SLACK] Channel: %s | Body: %s%n", channel, body);
        return true;
    }
}
