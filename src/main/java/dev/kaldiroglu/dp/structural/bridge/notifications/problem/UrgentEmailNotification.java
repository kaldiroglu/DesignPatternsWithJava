package dev.kaldiroglu.dp.structural.bridge.notifications.problem;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.DeliveryResult;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Message;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Transports;

/**
 * Naive design 2: a class for each (kind, channel) pair.
 * <p>
 * Cleaner than the switch — each class is short and does one thing. And it is exactly the
 * design GoF draw on p. 151, with exactly the same fault: the class name has to name both
 * axes, so the number of classes is their product.
 */
public final class UrgentEmailNotification {

    private final Transports transports;

    public UrgentEmailNotification(Transports transports) {
        this.transports = transports;
    }

    public DeliveryResult send(Recipient to, Message m) {
        boolean ok = transports.smtpSend(to.email(), m.subject(), m.body());
        int attempts = 1;
        if (!ok) {
            ok = transports.smtpSend(to.email(), m.subject(), m.body());
            attempts = 2;
        }
        return new DeliveryResult(ok, "email", to.email(), m.body(), attempts);
    }
}
