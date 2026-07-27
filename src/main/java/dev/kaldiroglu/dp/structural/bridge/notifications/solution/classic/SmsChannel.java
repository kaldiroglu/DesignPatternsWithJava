package dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Transports;

/**
 * A <b>ConcreteImplementor</b> whose vendor has opinions: 160 characters, no subject line,
 * and it throws if you exceed either.
 * <p>
 * Those opinions are answered here, once, and are then true for every kind of
 * notification that ever gets written. In the {@code problem} package the same rule had to
 * be remembered at four separate call sites — and was forgotten at one of them.
 */
public final class SmsChannel implements NotificationChannel {

    private final Transports transports;

    public SmsChannel(Transports transports) {
        this.transports = transports;
    }

    @Override
    public String name() {
        return "sms";
    }

    @Override
    public String addressOf(Recipient recipient) {
        return recipient.phone();
    }

    @Override
    public int maxBodyLength() {
        return Transports.SMS_LIMIT;
    }

    @Override
    public boolean supportsSubject() {
        return false;      // an SMS is one string; the abstraction folds the subject in
    }

    @Override
    public boolean deliver(String address, String subject, String body) {
        return transports.smsSubmit(address, body);
    }
}
