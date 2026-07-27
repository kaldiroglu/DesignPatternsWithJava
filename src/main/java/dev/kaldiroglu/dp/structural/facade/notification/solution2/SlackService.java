package dev.kaldiroglu.dp.structural.facade.notification.solution2;

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
