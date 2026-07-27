package dev.kaldiroglu.dp.structural.bridge.notifications;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.ChannelKind;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.DeliveryResult;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Message;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.TransportLog;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Transports;
import dev.kaldiroglu.dp.structural.bridge.notifications.problem.DigestEmailNotification;
import dev.kaldiroglu.dp.structural.bridge.notifications.problem.EmailBoundUrgentNotification;
import dev.kaldiroglu.dp.structural.bridge.notifications.problem.SwitchingNotifier;
import dev.kaldiroglu.dp.structural.bridge.notifications.problem.UrgentEmailNotification;
import dev.kaldiroglu.dp.structural.bridge.notifications.problem.UrgentSmsNotification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * All three naive designs work. Every test here passes, and that is the point: an argument
 * for a pattern is worth nothing if the alternative was never given a fair hearing. What
 * these tests then measure is the cost.
 */
class ProblemTest {

    private static final Message SHORT = new Message("Order shipped", "It is on its way.");
    private static final Message LONG = new Message("Payment failed",
            "We could not take payment for order 4021. Please update the card on file. "
            + "If the payment is not completed within 48 hours the order will be released "
            + "and the reserved stock returned to the warehouse for other customers.");

    private final TransportLog log = new TransportLog();
    private final Transports transports = new Transports(log);
    private final Recipient akin = Recipient.of("Akin", ChannelKind.EMAIL);

    // ------------------------------------------------- design 1: switch on both axes

    @Test
    @DisplayName("switch: it works, over every channel")
    void switchingWorks() {
        SwitchingNotifier notifier = new SwitchingNotifier(transports);

        assertTrue(notifier.send(SwitchingNotifier.Kind.SIMPLE, ChannelKind.EMAIL, akin, SHORT).delivered());
        assertTrue(notifier.send(SwitchingNotifier.Kind.URGENT, ChannelKind.SMS, akin, SHORT).delivered());
        assertTrue(notifier.send(SwitchingNotifier.Kind.SIMPLE, ChannelKind.PUSH, akin, SHORT).delivered());
        assertEquals(3, log.sendCount());
    }

    @Test
    @DisplayName("switch: the SMS rule is stated four times, so one branch forgot it")
    void theForgottenRule() {
        SwitchingNotifier notifier = new SwitchingNotifier(transports);

        // simple and urgent remembered the 160-character limit...
        assertTrue(notifier.send(SwitchingNotifier.Kind.SIMPLE, ChannelKind.SMS, akin, LONG).delivered());

        // ...and digest did not. The transport throws, in production, at 2am. Nobody
        // deleted the rule: the person who added digests never knew it existed, because
        // it was not written anywhere that a new branch would have to look.
        assertThrows(IllegalArgumentException.class,
                () -> notifier.send(SwitchingNotifier.Kind.DIGEST, ChannelKind.SMS, akin, LONG));
    }

    @Test
    @DisplayName("switch: kinds and channels multiply into branches")
    void branchesMultiply() {
        assertEquals(9, 3 * 3);        // and every one of them is written by hand
        assertEquals(12, 3 * 4);       // a fourth channel edits every kind's branch
    }

    // ------------------------------------------- design 2: a class per (kind, channel)

    @Test
    @DisplayName("class per pair: it works, and the retry is written once per channel")
    void classPerPairWorks() {
        DeliveryResult onEmail = new UrgentEmailNotification(transports).send(akin, SHORT);
        DeliveryResult onSms = new UrgentSmsNotification(transports).send(akin, SHORT);

        assertTrue(onEmail.delivered());
        assertTrue(onSms.delivered());
        assertEquals("email", onEmail.channel());
        assertEquals("sms", onSms.channel());

        // Both classes contain the same retry loop. It is the only thing that makes a
        // notification urgent, and it now exists in two places that cannot share it.
        assertEquals(2, log.sendCount());
    }

    @Test
    @DisplayName("class per pair: a retried failure costs two sends, in both copies")
    void bothCopiesRetry() {
        log.failNext(1);
        assertEquals(2, new UrgentEmailNotification(transports).send(akin, SHORT).attempts());

        log.failNext(1);
        assertEquals(2, new UrgentSmsNotification(transports).send(akin, SHORT).attempts());
    }

    @Test
    @DisplayName("class per pair: the class count is the product of the two axes")
    void classesMultiply() {
        int kinds = 3, channels = 3;
        assertEquals(9, kinds * channels);
        assertEquals(12, kinds * (channels + 1));   // a fourth channel: three new classes
        assertEquals(12, (kinds + 1) * channels);   // a fourth kind: three new classes
    }

    // ------------------------------------------------ design 3: inherit the channel

    @Test
    @DisplayName("inherit the channel: it works, and it ignores what the user asked for")
    void inheritanceIgnoresPreference() {
        Recipient prefersSms = Recipient.of("Bora", ChannelKind.SMS);
        DeliveryResult result = new EmailBoundUrgentNotification(transports).send(prefersSms, SHORT);

        assertTrue(result.delivered());
        assertEquals(ChannelKind.SMS, prefersSms.preferred());
        assertEquals("email", result.channel());     // sent the wrong way, successfully
        assertNotEquals(prefersSms.preferred().name().toLowerCase(), result.channel());
    }

    @Test
    @DisplayName("inherit the channel: the channel is the type, so it cannot be chosen at run time")
    void channelIsTheType() {
        // EmailBoundUrgentNotification IS an EmailSender. There is no operation, no
        // setter and no configuration that can make this object send an SMS - a
        // superclass is chosen when the code is compiled, and never again.
        assertTrue(dev.kaldiroglu.dp.structural.bridge.notifications.problem.EmailSender.class
                .isAssignableFrom(EmailBoundUrgentNotification.class));
    }

    @Test
    @DisplayName("all three designs put the same messages on the wire")
    void allThreeWork() {
        new UrgentEmailNotification(transports).send(akin, SHORT);
        new DigestEmailNotification(transports).send(akin, SHORT);
        new EmailBoundUrgentNotification(transports).send(akin, SHORT);
        new SwitchingNotifier(transports).send(SwitchingNotifier.Kind.SIMPLE, ChannelKind.EMAIL, akin, SHORT);

        assertEquals(4, log.sendCount("email"));
    }
}
