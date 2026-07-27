package dev.kaldiroglu.dp.structural.bridge.notifications.problem;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.DeliveryResult;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Message;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Transports;

/**
 * The same "urgent" idea, over a different channel.
 * <p>
 * Put this class beside {@link UrgentEmailNotification} and read them together. The retry
 * — the thing that makes a notification urgent — is written twice, and it will be written
 * a third time when push arrives.
 */
public final class UrgentSmsNotification {

    private final Transports transports;

    public UrgentSmsNotification(Transports transports) {
        this.transports = transports;
    }

    public DeliveryResult send(Recipient to, Message m) {
        String text = "URGENT " + m.subject() + ": " + m.body();
        if (text.length() > Transports.SMS_LIMIT) {
            text = text.substring(0, Transports.SMS_LIMIT);
        }
        boolean ok = transports.smsSubmit(to.phone(), text);
        int attempts = 1;
        if (!ok) {
            ok = transports.smsSubmit(to.phone(), text);
            attempts = 2;
        }
        return new DeliveryResult(ok, "sms", to.phone(), text, attempts);
    }
}
