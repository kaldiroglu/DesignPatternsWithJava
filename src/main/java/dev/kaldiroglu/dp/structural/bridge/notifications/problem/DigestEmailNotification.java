package dev.kaldiroglu.dp.structural.bridge.notifications.problem;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.DeliveryResult;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Message;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Transports;

/** A third pair. Two axes, and the classes keep arriving at their product. */
public final class DigestEmailNotification {

    private final Transports transports;

    public DigestEmailNotification(Transports transports) {
        this.transports = transports;
    }

    public DeliveryResult send(Recipient to, Message m) {
        String combined = "Digest: " + m.body();
        boolean ok = transports.smtpSend(to.email(), "Your digest", combined);
        return new DeliveryResult(ok, "email", to.email(), combined, 1);
    }
}
