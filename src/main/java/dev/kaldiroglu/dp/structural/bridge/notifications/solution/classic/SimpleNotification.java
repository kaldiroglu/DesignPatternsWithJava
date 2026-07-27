package dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.DeliveryResult;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Message;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;

/** A <b>RefinedAbstraction</b>: say it once, over whatever channel is in use. */
public final class SimpleNotification extends Notification {

    public SimpleNotification(NotificationChannel channel) {
        super(channel);
    }

    @Override
    public DeliveryResult notify(Recipient to, Message message) {
        return dispatch(to, message, 1);
    }
}
