package dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.DeliveryResult;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Message;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;

import java.util.ArrayList;
import java.util.List;

/**
 * A <b>RefinedAbstraction</b> that holds real state: collect several messages, then send
 * one.
 * <p>
 * It is worth showing because it proves the abstraction hierarchy is a hierarchy and not
 * decoration. This class has its own data and its own lifecycle, and still says nothing
 * about any channel.
 */
public final class DigestNotification extends Notification {

    private final List<Message> pending = new ArrayList<>();

    public DigestNotification(NotificationChannel channel) {
        super(channel);
    }

    /** Add a message to the digest. Nothing is sent yet. */
    public DigestNotification add(Message message) {
        pending.add(message);
        return this;
    }

    public int pendingCount() {
        return pending.size();
    }

    @Override
    public DeliveryResult notify(Recipient to, Message message) {
        add(message);
        return flush(to);
    }

    /** Send everything collected so far as a single message. */
    public DeliveryResult flush(Recipient to) {
        String body = String.join(" | ", pending.stream().map(Message::body).toList());
        Message combined = new Message("Your digest (" + pending.size() + ")", body);
        pending.clear();
        // fit() is inherited: whatever the channel's limit is, the digest respects it.
        return dispatch(to, combined, 1);
    }
}
