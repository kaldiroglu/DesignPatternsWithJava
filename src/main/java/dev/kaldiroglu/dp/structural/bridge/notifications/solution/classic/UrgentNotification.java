package dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.DeliveryResult;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Message;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;

/**
 * A <b>RefinedAbstraction</b>: mark it urgent, and try again if the channel refuses it.
 * <p>
 * The retry lives here, once, and it is now true of urgent notifications over every
 * channel that exists — and over every channel added later. The {@code problem} package
 * needed one copy of this loop per channel.
 */
public final class UrgentNotification extends Notification {

    private final int maxAttempts;

    public UrgentNotification(NotificationChannel channel) {
        this(channel, 2);
    }

    public UrgentNotification(NotificationChannel channel, int maxAttempts) {
        super(channel);
        this.maxAttempts = maxAttempts;
    }

    @Override
    public DeliveryResult notify(Recipient to, Message message) {
        Message marked = new Message("URGENT " + message.subject(), message.body());

        DeliveryResult result = dispatch(to, marked, 1);
        for (int attempt = 2; attempt <= maxAttempts && !result.delivered(); attempt++) {
            result = dispatch(to, marked, attempt);
        }
        return result;
    }
}
