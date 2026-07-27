package dev.kaldiroglu.dp.structural.bridge.notifications.solution.shared;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.NotificationChannel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Variation 2 — <b>sharing implementors</b> (GoF implementation issue 3, p. 155).
 * <p>
 * A channel is usually expensive: an SMTP connection, an HTTP client, a vendor session.
 * Nothing in the pattern says every abstraction needs its own — several notifications can
 * point at the same implementor object, and usually should.
 * <p>
 * This wrapper makes the sharing visible. It counts how many abstractions are using it and
 * how many messages have gone through it, so a test can show one channel serving many
 * notifications.
 *
 * <h2>What it costs</h2>
 * A shared implementor is shared <em>state</em>. It must be thread-safe, its failures are
 * everybody's failures, and it cannot hold anything specific to one abstraction. GoF's
 * C++ answer was reference counting; in a managed language the question is usually
 * lifecycle and thread safety instead.
 */
public final class PooledChannel implements NotificationChannel {

    private final NotificationChannel delegate;
    private final AtomicInteger users = new AtomicInteger();
    private final AtomicInteger messages = new AtomicInteger();

    public PooledChannel(NotificationChannel delegate) {
        this.delegate = delegate;
    }

    /** Called when an abstraction starts using this channel. */
    public PooledChannel acquire() {
        users.incrementAndGet();
        return this;
    }

    public int users() {
        return users.get();
    }

    public int messagesSent() {
        return messages.get();
    }

    @Override
    public String name() {
        return delegate.name();
    }

    @Override
    public String addressOf(Recipient recipient) {
        return delegate.addressOf(recipient);
    }

    @Override
    public int maxBodyLength() {
        return delegate.maxBodyLength();
    }

    @Override
    public boolean supportsSubject() {
        return delegate.supportsSubject();
    }

    @Override
    public boolean deliver(String address, String subject, String body) {
        messages.incrementAndGet();
        return delegate.deliver(address, subject, body);
    }
}
