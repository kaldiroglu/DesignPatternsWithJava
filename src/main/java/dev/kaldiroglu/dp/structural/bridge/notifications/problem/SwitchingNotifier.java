package dev.kaldiroglu.dp.structural.bridge.notifications.problem;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.ChannelKind;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.DeliveryResult;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Message;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Transports;

/**
 * Naive design 1: one class, one method, and a switch on each axis.
 * <p>
 * This is what the code looks like after the second channel arrives and before anybody
 * has time to think. It works, and for two kinds and two channels it is arguably the
 * clearest thing in the file.
 *
 * <h2>What it costs</h2>
 * <ul>
 *   <li><b>Every branch is a pair.</b> Kinds x channels, written out by hand. Three kinds
 *       and three channels is nine branches in one method — and the ninth is written by
 *       somebody who has forgotten what the first one does.</li>
 *   <li><b>Both axes are frozen together.</b> Adding a channel means editing every kind's
 *       branch; adding a kind means editing every channel's. There is no edit that touches
 *       one axis alone.</li>
 *   <li><b>The rules leak.</b> Look at how many times {@code SMS_LIMIT} appears below. The
 *       SMS length rule is not owned by anything; it is repeated wherever somebody
 *       remembered it — and, in {@code sendDigest}, forgotten.</li>
 * </ul>
 */
public final class SwitchingNotifier {

    /** What kind of notification this is. The second axis of the problem. */
    public enum Kind {
        SIMPLE, URGENT, DIGEST
    }

    private final Transports transports;

    public SwitchingNotifier(Transports transports) {
        this.transports = transports;
    }

    public DeliveryResult send(Kind kind, ChannelKind channel, Recipient to, Message message) {
        return switch (kind) {
            case SIMPLE -> sendSimple(channel, to, message);
            case URGENT -> sendUrgent(channel, to, message);
            case DIGEST -> sendDigest(channel, to, message);
        };
    }

    private DeliveryResult sendSimple(ChannelKind channel, Recipient to, Message m) {
        return switch (channel) {
            case EMAIL -> {
                boolean ok = transports.smtpSend(to.email(), m.subject(), m.body());
                yield new DeliveryResult(ok, "email", to.email(), m.body(), 1);
            }
            case SMS -> {
                String text = clip(m.subject() + ": " + m.body(), Transports.SMS_LIMIT);
                boolean ok = transports.smsSubmit(to.phone(), text);
                yield new DeliveryResult(ok, "sms", to.phone(), text, 1);
            }
            case PUSH -> {
                String payload = clip(m.subject(), Transports.PUSH_LIMIT);
                boolean ok = transports.pushNotify(to.deviceToken(), payload);
                yield new DeliveryResult(ok, "push", to.deviceToken(), payload, 1);
            }
        };
    }

    private DeliveryResult sendUrgent(ChannelKind channel, Recipient to, Message m) {
        // "Urgent" means: try again once if it fails. The retry has to be written out
        // separately for every channel, because there is nothing common to hang it on.
        return switch (channel) {
            case EMAIL -> {
                boolean ok = transports.smtpSend(to.email(), m.subject(), m.body());
                int attempts = 1;
                if (!ok) {
                    ok = transports.smtpSend(to.email(), m.subject(), m.body());
                    attempts = 2;
                }
                yield new DeliveryResult(ok, "email", to.email(), m.body(), attempts);
            }
            case SMS -> {
                String text = clip("URGENT " + m.subject() + ": " + m.body(), Transports.SMS_LIMIT);
                boolean ok = transports.smsSubmit(to.phone(), text);
                int attempts = 1;
                if (!ok) {
                    ok = transports.smsSubmit(to.phone(), text);
                    attempts = 2;
                }
                yield new DeliveryResult(ok, "sms", to.phone(), text, attempts);
            }
            case PUSH -> {
                String payload = clip("URGENT " + m.subject(), Transports.PUSH_LIMIT);
                boolean ok = transports.pushNotify(to.deviceToken(), payload);
                int attempts = 1;
                if (!ok) {
                    ok = transports.pushNotify(to.deviceToken(), payload);
                    attempts = 2;
                }
                yield new DeliveryResult(ok, "push", to.deviceToken(), payload, attempts);
            }
        };
    }

    private DeliveryResult sendDigest(ChannelKind channel, Recipient to, Message m) {
        String combined = "Digest: " + m.body();
        return switch (channel) {
            case EMAIL -> {
                boolean ok = transports.smtpSend(to.email(), "Your digest", combined);
                yield new DeliveryResult(ok, "email", to.email(), combined, 1);
            }
            case SMS -> {
                // The 160-character rule is missing here. Nobody removed it; the person
                // who added digests simply did not know it existed. The transport throws.
                boolean ok = transports.smsSubmit(to.phone(), combined);
                yield new DeliveryResult(ok, "sms", to.phone(), combined, 1);
            }
            case PUSH -> {
                String payload = clip(combined, Transports.PUSH_LIMIT);
                boolean ok = transports.pushNotify(to.deviceToken(), payload);
                yield new DeliveryResult(ok, "push", to.deviceToken(), payload, 1);
            }
        };
    }

    private static String clip(String text, int limit) {
        return text.length() <= limit ? text : text.substring(0, limit);
    }
}
