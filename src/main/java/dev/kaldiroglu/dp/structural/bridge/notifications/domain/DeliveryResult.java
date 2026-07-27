package dev.kaldiroglu.dp.structural.bridge.notifications.domain;

/** What actually happened when we tried to reach somebody. */
public record DeliveryResult(boolean delivered, String channel, String address,
                             String bodySent, int attempts) {

    public boolean truncated(String originalBody) {
        return bodySent.length() < originalBody.length();
    }
}
