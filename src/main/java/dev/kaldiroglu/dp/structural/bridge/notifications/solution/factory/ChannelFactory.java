package dev.kaldiroglu.dp.structural.bridge.notifications.solution.factory;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.NotificationChannel;

/**
 * Variation 1 — <b>who chooses the implementor?</b>
 * <p>
 * GoF raise this as implementation issue 2 (p. 155): "How, where, and when do you decide
 * which Implementor class to instantiate?" In the classic version above, the caller does
 * it by writing {@code new SmsChannel(..)} — which means every caller now knows the names
 * of every channel, and the choice is made where the notification is created rather than
 * where the knowledge lives.
 * <p>
 * A factory moves that decision to one place. The abstraction never names a channel again.
 */
@FunctionalInterface
public interface ChannelFactory {

    /** The channel this recipient should be reached on, right now. */
    NotificationChannel channelFor(Recipient recipient);
}
