package dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.DeliveryResult;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Message;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;

import java.util.Objects;

/**
 * The <b>Abstraction</b>: what a notification is, to the rest of the application.
 * <p>
 * One field, and it is the entire solution. This class does not extend a channel — it
 * <em>holds</em> one, and it can be handed a different one at any moment, including after
 * it has been constructed.
 * <p>
 * Read {@link #dispatch} carefully. It is written entirely in terms of the implementor's
 * primitives: ask the channel for the address, ask it how much text it will take, ask it
 * whether it has a subject line. It never asks <em>which</em> channel it is talking to,
 * and there is no {@code if (channel instanceof SmsChannel)} anywhere in this package.
 * The moment such a line appears, the bridge is gone.
 */
public abstract class Notification {

    private NotificationChannel channel;

    protected Notification(NotificationChannel channel) {
        this.channel = Objects.requireNonNull(channel, "a notification needs a channel");
    }

    /** The channel currently in use. */
    protected final NotificationChannel channel() {
        return channel;
    }

    public final String channelName() {
        return channel.name();
    }

    /**
     * Point this notification at a different channel.
     * <p>
     * Nothing in the {@code problem} package can do this: there, the channel is the
     * class, so a user who changes their preference needs a different object.
     */
    public final void setChannel(NotificationChannel newChannel) {
        this.channel = Objects.requireNonNull(newChannel);
    }

    /** What this <em>kind</em> of notification does. The refinements below define it. */
    public abstract DeliveryResult notify(Recipient to, Message message);

    // --- the higher-level operation, built from primitives --------------------------

    /**
     * Hand one message to whatever channel is in use, shaped to fit it.
     * <p>
     * This is the method every refinement is written on top of, and it is the reason the
     * SMS length rule is stated exactly once in this design.
     */
    protected final DeliveryResult dispatch(Recipient to, Message message, int attempts) {
        String address = channel.addressOf(to);
        String body = channel.supportsSubject()
                ? message.body()
                : message.subject() + ": " + message.body();
        body = fit(body);

        boolean delivered = channel.deliver(address, message.subject(), body);
        return new DeliveryResult(delivered, channel.name(), address, body, attempts);
    }

    /** Trim a body to whatever the channel in use will carry. */
    protected final String fit(String body) {
        int limit = channel.maxBodyLength();
        return body.length() <= limit ? body : body.substring(0, limit);
    }
}
