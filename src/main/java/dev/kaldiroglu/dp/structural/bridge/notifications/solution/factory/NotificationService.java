package dev.kaldiroglu.dp.structural.bridge.notifications.solution.factory;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.DeliveryResult;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Message;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.Notification;

import java.util.function.Function;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.NotificationChannel;

/**
 * What the application actually calls.
 * <p>
 * Note what this class does <em>not</em> contain: any channel name, any {@code switch},
 * and any knowledge of who prefers what. It is handed a factory and a notification kind,
 * and the two axes meet here — for one line, at run time, and nowhere else.
 */
public final class NotificationService {

    private final ChannelFactory factory;

    public NotificationService(ChannelFactory factory) {
        this.factory = factory;
    }

    /**
     * @param kind how to build the notification, once the channel is known — for example
     *             {@code UrgentNotification::new}
     */
    public DeliveryResult send(Function<NotificationChannel, Notification> kind,
                               Recipient to, Message message) {
        Notification notification = kind.apply(factory.channelFor(to));
        return notification.notify(to, message);
    }
}
