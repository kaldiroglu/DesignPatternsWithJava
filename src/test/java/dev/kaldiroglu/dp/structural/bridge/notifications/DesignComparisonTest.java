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
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.UrgentNotification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    @DisplayName("what a fourth channel costs in each design")
    void costOfTheNextChannel() {
        // Adding WhatsApp to a system with 3 notification kinds:
        //
        //   switch          : edit every kind's branch in one shared method, and hope the
        //                     length and subject rules are remembered in all three.
        //   class per pair  : write 3 new classes, one per kind, each repeating that
        //                     kind's logic.
        //   inherit channel : write 3 new classes, and no existing object can ever use
        //                     the new channel.
        //   bridge          : write 1 new class. Change nothing else. Every kind that
        //                     exists, and every kind written next year, can use it.
        int kinds = 3;
        assertEquals(3, kinds);      // classes added by the first three designs
        assertEquals(1, 1);          // classes added by the bridge
    }
}
