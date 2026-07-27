package dev.kaldiroglu.dp.structural.bridge.notifications.problem;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.DeliveryResult;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Message;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Transports;

/**
 * Naive design 3, part two: the notification kind inherits the channel.
 * <p>
 * This is the design that feels cleanest of the three, and it is the one that hurts most,
 * because of what its type says. An {@code EmailBoundUrgentNotification} <b>is an</b>
 * {@link EmailSender}. Not "has a channel" — <em>is</em> one. So:
 * <ul>
 *   <li>the channel is fixed when the class is compiled, and a user who prefers SMS cannot
 *       be served by this object at any price;</li>
 *   <li>the retry logic — the only part that is really about urgency — is trapped inside
 *       an email class, and the SMS version will have to copy it;</li>
 *   <li>and a second channel is not a new class here, it is a new class <em>per kind</em>.</li>
 * </ul>
 * GoF put it plainly (p. 151): "it makes client code platform-dependent" and "makes it
 * hard to extend the abstraction and its implementation independently".
 */
public final class EmailBoundUrgentNotification extends EmailSender {

    public EmailBoundUrgentNotification(Transports transports) {
        super(transports);
    }

    public DeliveryResult send(Recipient to, Message m) {
        boolean ok = deliver(to.email(), m.subject(), m.body());
        int attempts = 1;
        if (!ok) {
            ok = deliver(to.email(), m.subject(), m.body());
            attempts = 2;
        }
        return new DeliveryResult(ok, channelName(), to.email(), m.body(), attempts);
    }
}
