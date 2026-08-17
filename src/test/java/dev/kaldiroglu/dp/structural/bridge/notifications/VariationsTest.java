package dev.kaldiroglu.dp.structural.bridge.notifications;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.ChannelKind;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.DeliveryResult;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Message;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.TransportLog;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Transports;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.DigestNotification;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.EmailChannel;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.Notification;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.SimpleNotification;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.UrgentNotification;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.factory.NotificationService;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.factory.PreferenceChannelFactory;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.shared.PooledChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The two variations. Neither changes the solution; each answers a question the classic
 * version leaves to the caller.
 */
class VariationsTest {

    private static final Message SHORT = new Message("Order shipped", "It is on its way.");

    private final TransportLog log = new TransportLog();
    private final Transports transports = new Transports(log);

    // ------------------------------------------------- variation 1: a factory chooses

    @Test
    @DisplayName("factory: each recipient is reached the way they asked to be")
    void factoryHonoursPreference() {
        var service = new NotificationService(new PreferenceChannelFactory(transports));

        DeliveryResult toAkin = service.send(UrgentNotification::new,
                Recipient.of("Akin", ChannelKind.EMAIL), SHORT);
        DeliveryResult toBora = service.send(UrgentNotification::new,
                Recipient.of("Bora", ChannelKind.SMS), SHORT);
        DeliveryResult toCeyda = service.send(UrgentNotification::new,
                Recipient.of("Ceyda", ChannelKind.PUSH), SHORT);

        assertEquals("email", toAkin.channel());
        assertEquals("sms", toBora.channel());
        assertEquals("push", toCeyda.channel());

        // One call site, one notification kind, three channels — decided by a value that
        // came out of a database while the program was running. This is the test that
        // cannot be written at all against the inheritance design.
        assertEquals(3, log.sendCount());
    }

    @Test
    @DisplayName("factory: the notification kind and the channel are chosen independently")
    void kindAndChannelAreIndependent() {
        var service = new NotificationService(new PreferenceChannelFactory(transports));
        Recipient bora = Recipient.of("Bora", ChannelKind.SMS);

        assertEquals("sms", service.send(SimpleNotification::new, bora, SHORT).channel());
        assertEquals("sms", service.send(UrgentNotification::new, bora, SHORT).channel());
        assertEquals("sms", service.send(DigestNotification::new, bora, SHORT).channel());

        // Three kinds down one channel, and the caller named neither a channel class nor
        // a combination class.
        assertEquals(3, log.sendCount("sms"));
    }

    @Test
    @DisplayName("factory: registering a fourth channel touches no notification kind")
    void registeringAChannel() {
        var factory = new PreferenceChannelFactory(transports);
        var email = new EmailChannel(transports);

        // Re-point PUSH at the email channel — a one-line operational change of the sort
        // that happens when a vendor goes down.
        factory.register(ChannelKind.PUSH, email);
        var service = new NotificationService(factory);

        DeliveryResult result = service.send(UrgentNotification::new,
                Recipient.of("Ceyda", ChannelKind.PUSH), SHORT);

        assertEquals("email", result.channel());
        assertEquals(0, log.sendCount("push"));
    }

    // --------------------------------------------- variation 2: sharing an implementor

    @Test
    @DisplayName("shared: one channel object serves many notifications")
    void oneChannelManyNotifications() {
        var pooled = new PooledChannel(new EmailChannel(transports));

        List<Notification> notifications = List.of(
                new SimpleNotification(pooled.acquire()),
                new UrgentNotification(pooled.acquire()),
                new DigestNotification(pooled.acquire()));

        Recipient akin = Recipient.of("Akin", ChannelKind.EMAIL);
        notifications.forEach(n -> n.notify(akin, SHORT));

        assertEquals(3, pooled.users());          // three abstractions, one implementor
        assertEquals(3, pooled.messagesSent());
        assertEquals(3, log.sendCount("email"));
    }

    @Test
    @DisplayName("shared: the abstractions really do hold the same object")
    void sameInstance() {
        var pooled = new PooledChannel(new EmailChannel(transports));
        var a = new SimpleNotification(pooled.acquire());
        var b = new UrgentNotification(pooled.acquire());

        // Both notifications point at one channel. That is the saving — one connection,
        // one client, one vendor session — and also the cost: shared mutable state, so
        // it has to be thread-safe, and its failures belong to everybody at once.
        assertSame(pooled, extractChannel(a));
        assertSame(pooled, extractChannel(b));
        assertEquals(2, pooled.users());
    }

    @Test
    @DisplayName("shared: a pooled channel behaves exactly like the channel it wraps")
    void poolingIsTransparent() {
        var direct = new UrgentNotification(new EmailChannel(transports));
        var viaPool = new UrgentNotification(new PooledChannel(new EmailChannel(transports)).acquire());
        Recipient akin = Recipient.of("Akin", ChannelKind.EMAIL);

        DeliveryResult a = direct.notify(akin, SHORT);
        DeliveryResult b = viaPool.notify(akin, SHORT);

        assertEquals(a.channel(), b.channel());
        assertEquals(a.bodySent(), b.bodySent());
        assertTrue(a.delivered() && b.delivered());
    }

    /** Reads the implementor back out of an abstraction, for the identity check above. */
    private static Object extractChannel(Notification notification) {
        try {
            var field = Notification.class.getDeclaredField("channel");
            field.setAccessible(true);
            return field.get(notification);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }
}
