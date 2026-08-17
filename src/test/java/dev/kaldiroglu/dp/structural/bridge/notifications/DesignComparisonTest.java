package dev.kaldiroglu.dp.structural.bridge.notifications;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.ChannelKind;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.DeliveryResult;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Message;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.TransportLog;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Transports;
import dev.kaldiroglu.dp.structural.bridge.notifications.problem.SwitchingNotifier;
import dev.kaldiroglu.dp.structural.bridge.notifications.problem.UrgentEmailNotification;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.EmailChannel;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.SimpleNotification;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.NotificationChannel;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.DigestNotification;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.UrgentNotification;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The designs are only worth comparing if they do the same job. These tests run one
 * scenario through all of them and check they put the same thing on the wire, so every
 * remaining difference is a difference of design and not of behavior.
 */
class DesignComparisonTest {

    private static final Message SHORT = new Message("Order shipped", "It is on its way.");
    private final Recipient akin = Recipient.of("Akin", ChannelKind.EMAIL);

    private TransportLog.Sent onlySend(java.util.function.Consumer<Transports> scenario) {
        TransportLog log = new TransportLog();
        scenario.accept(new Transports(log));
        assertEquals(1, log.sendCount());
        return log.sends().getFirst();
    }

    @Test
    @DisplayName("all three designs send the same urgent email")
    void designsAgree() {
        TransportLog.Sent bySwitch = onlySend(t -> new SwitchingNotifier(t)
                .send(SwitchingNotifier.Kind.URGENT, ChannelKind.EMAIL, akin, SHORT));
        TransportLog.Sent byPairClass = onlySend(t -> new UrgentEmailNotification(t).send(akin, SHORT));
        TransportLog.Sent byBridge = onlySend(t -> new UrgentNotification(new EmailChannel(t))
                .notify(akin, SHORT));

        assertEquals(bySwitch.channel(), byBridge.channel());
        assertEquals(bySwitch.address(), byBridge.address());
        assertEquals(byPairClass.channel(), byBridge.channel());
        assertEquals(byPairClass.body(), byBridge.body());
    }

    /**
     * What a fourth channel costs, demonstrated rather than counted.
     * <p>
     * Adding WhatsApp to a system with three notification kinds means, in the three naive
     * designs, editing every kind's branch or writing one class per kind — and in the
     * inheritance design no object that already exists can ever use the new channel at all.
     * <p>
     * None of that is asserted here as arithmetic, because it was: this method used to say
     * {@code assertEquals(3, kinds)} and {@code assertEquals(1, 1)}, neither of which can
     * fail. The bridge half is the half that can actually be shown, so it is shown — a
     * channel written inside this method, used by every kind that already exists, with not
     * one of them touched. The counted version of the class arithmetic lives in
     * {@code ClassicBridgeTest.theArithmetic}, which reads the package.
     */
    @Test
    @DisplayName("a fourth channel is one class here, and every existing kind can use it")
    void costOfTheNextChannel() {
        // The whole of a fourth channel. Nothing above it is edited, or even recompiled.
        NotificationChannel whatsApp = new NotificationChannel() {
            public String name() {
                return "whatsapp";
            }

            public String addressOf(Recipient recipient) {
                return recipient.phone();
            }

            public int maxBodyLength() {
                return 4096;
            }

            public boolean supportsSubject() {
                return false;
            }

            public boolean deliver(String address, String subject, String body) {
                return true;
            }
        };

        // Every kind that already exists, reaching a channel written after all of them —
        // and the result names this channel, so the abstraction really did use it.
        for (DeliveryResult result : List.of(
                new SimpleNotification(whatsApp).notify(akin, SHORT),
                new UrgentNotification(whatsApp).notify(akin, SHORT),
                new DigestNotification(whatsApp).add(SHORT).notify(akin, SHORT))) {
            assertTrue(result.delivered());
            assertEquals("whatsapp", result.channel());
        }
    }
}
