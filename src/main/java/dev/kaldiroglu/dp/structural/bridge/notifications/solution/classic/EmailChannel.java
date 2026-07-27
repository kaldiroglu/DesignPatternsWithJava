package dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Transports;

/**
 * A <b>ConcreteImplementor</b>. It knows one vendor SDK and nothing else — not what an
 * urgent notification is, not what a digest is, and it will never need to change when
 * either of them changes.
 */
public final class EmailChannel implements NotificationChannel {

    private final Transports transports;

    public EmailChannel(Transports transports) {
        this.transports = transports;
    }

    @Override
    public String name() {
        return "email";
    }

    @Override
    public String addressOf(Recipient recipient) {
        return recipient.email();
    }

    @Override
    public int maxBodyLength() {
        return Transports.EMAIL_LIMIT;
    }

    @Override
    public boolean supportsSubject() {
        return true;
    }

    @Override
    public boolean deliver(String address, String subject, String body) {
        return transports.smtpSend(address, subject, body);
    }
}
