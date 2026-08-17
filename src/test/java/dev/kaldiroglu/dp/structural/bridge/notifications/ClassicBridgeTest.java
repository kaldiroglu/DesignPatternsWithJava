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
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.NotificationChannel;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.PushChannel;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.SimpleNotification;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.SmsChannel;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.UrgentNotification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The two hierarchies, and the ways they meet. The point of almost every test here is
 * that a notification kind is tested once and is then true over every channel — including
 * channels that did not exist when it was written.
 */
class ClassicBridgeTest {

    private static final Message SHORT = new Message("Order shipped", "It is on its way.");
    private static final Message LONG = new Message("Payment failed",
            "We could not take payment for order 4021. Please update the card on file. "
            + "If the payment is not completed within 48 hours the order will be released "
            + "and the reserved stock returned to the warehouse for other customers.");

    private final TransportLog log = new TransportLog();
    private final Transports transports = new Transports(log);
    private final Recipient akin = Recipient.of("Akin", ChannelKind.EMAIL);

    private List<NotificationChannel> allChannels() {
        return List.of(new EmailChannel(transports), new SmsChannel(transports),
                new PushChannel(transports));
    }

    @Test
    @DisplayName("every kind works over every channel — 3 x 3, from 3 + 3 classes")
    void everyKindOverEveryChannel() {
        for (NotificationChannel channel : allChannels()) {
            assertTrue(new SimpleNotification(channel).notify(akin, SHORT).delivered());
            assertTrue(new UrgentNotification(channel).notify(akin, SHORT).delivered());
            assertTrue(new DigestNotification(channel).add(SHORT).notify(akin, SHORT).delivered());
        }
        assertEquals(9, log.sendCount());
    }

    @Test
    @DisplayName("the retry lives in one place and is true over every channel")
    void retryIsWrittenOnce() {
        for (NotificationChannel channel : allChannels()) {
            log.failNext(1);
            assertEquals(2, new UrgentNotification(channel).notify(akin, SHORT).attempts());
        }
        // Three channels, one retry loop, six sends: two attempts on each.
        assertEquals(6, log.sendCount());
    }

    @Test
    @DisplayName("the channel's limit is asked for, not assumed — and every kind respects it")
    void theAbstractionAsksForLimits() {
        DeliveryResult onEmail = new UrgentNotification(new EmailChannel(transports)).notify(akin, LONG);
        DeliveryResult onSms = new UrgentNotification(new SmsChannel(transports)).notify(akin, LONG);
        DeliveryResult onPush = new UrgentNotification(new PushChannel(transports)).notify(akin, LONG);

        assertFalse(onEmail.truncated(LONG.body()));
        assertEquals(Transports.SMS_LIMIT, onSms.bodySent().length());
        assertEquals(Transports.PUSH_LIMIT, onPush.bodySent().length());

        // The digest — the kind that forgot the rule in the problem package — gets it for
        // free here, because the rule belongs to the channel rather than to a call site.
        DeliveryResult digest = new DigestNotification(new SmsChannel(transports))
                .add(LONG).add(LONG).notify(akin, LONG);
        assertTrue(digest.delivered());
        assertEquals(Transports.SMS_LIMIT, digest.bodySent().length());
    }

    @Test
    @DisplayName("a channel without a subject line gets one folded into the body")
    void channelsWithoutSubjects() {
        DeliveryResult onSms = new SimpleNotification(new SmsChannel(transports)).notify(akin, SHORT);

        assertTrue(onSms.bodySent().startsWith("Order shipped: "));
        // The abstraction asked supportsSubject(). It did not ask what kind of channel
        // this is, and there is no instanceof anywhere in the solution package.
    }

    @Test
    @DisplayName("the channel can be swapped on a notification that already exists")
    void channelIsChosenAtRunTime() {
        Notification notification = new UrgentNotification(new EmailChannel(transports));
        assertEquals("email", notification.channelName());
        notification.notify(akin, SHORT);

        notification.setChannel(new SmsChannel(transports));    // same object

        assertEquals("sms", notification.channelName());
        notification.notify(akin, SHORT);
        assertEquals(1, log.sendCount("email"));
        assertEquals(1, log.sendCount("sms"));
    }

    @Test
    @DisplayName("a refined abstraction may hold state and still know nothing about channels")
    void refinementsMayHaveState() {
        DigestNotification digest = new DigestNotification(new EmailChannel(transports));
        digest.add(new Message("A", "first")).add(new Message("B", "second"));
        assertEquals(2, digest.pendingCount());
        assertEquals(0, log.sendCount());          // nothing sent yet

        DeliveryResult result = digest.flush(akin);
        assertEquals(1, log.sendCount());          // one message carrying both
        assertTrue(result.bodySent().contains("first"));
        assertTrue(result.bodySent().contains("second"));
        assertEquals(0, digest.pendingCount());
    }

    @Test
    @DisplayName("a new channel costs one class and works with every kind at once")
    void addingAChannelCostsOneClass() {
        // A fax channel, invented here in a dozen lines. No notification kind is touched,
        // and all three of them can use it immediately.
        NotificationChannel fax = new NotificationChannel() {
            public String name() {
                return "fax";
            }

            public String addressOf(Recipient recipient) {
                return recipient.phone();
            }

            public int maxBodyLength() {
                return 2000;
            }

            public boolean supportsSubject() {
                return true;
            }

            public boolean deliver(String address, String subject, String body) {
                return true;
            }
        };

        assertTrue(new SimpleNotification(fax).notify(akin, SHORT).delivered());
        assertTrue(new UrgentNotification(fax).notify(akin, SHORT).delivered());
        assertTrue(new DigestNotification(fax).add(SHORT).notify(akin, SHORT).delivered());
    }

    @Test
    @DisplayName("a new kind costs one class and works over every channel at once")
    void addingAKindCostsOneClass() {
        // A "quiet" notification that never retries and always trims hard, written once.
        class QuietNotification extends Notification {
            QuietNotification(NotificationChannel channel) {
                super(channel);
            }

            public DeliveryResult notify(Recipient to, Message message) {
                return dispatch(to, new Message(message.subject(), fit(message.body())), 1);
            }
        }

        for (NotificationChannel channel : allChannels()) {
            assertTrue(new QuietNotification(channel).notify(akin, LONG).delivered());
        }
        assertEquals(3, log.sendCount());
    }

    /**
     * Every class file in a package, loaded. Nested and anonymous classes are skipped:
     * only the top-level types a reader would count on a class diagram are returned.
     */
    private static List<Class<?>> typesIn(String packageName) throws Exception {
        String resource = packageName.replace('.', '/');
        URL url = ClassicBridgeTest.class.getClassLoader().getResource(resource);
        assertNotNull(url, "package not on the test classpath: " + packageName);
        try (Stream<Path> files = Files.list(Path.of(url.toURI()))) {
            List<Class<?>> types = new ArrayList<>();
            for (Path file : files.sorted().toList()) {
                String name = file.getFileName().toString();
                if (!name.endsWith(".class") || name.contains("$")) {
                    continue;
                }
                types.add(Class.forName(packageName + '.' + name.substring(0, name.length() - 6)));
            }
            return types;
        }
    }

    /**
     * The class counts quoted on the slides, counted from the package rather than asserted
     * as arithmetic on literals. Writing {@code assertEquals(6, 3 + 3)} proves something
     * about integers; this fails the day somebody adds a fourth channel and leaves the
     * slide saying six.
     */
    @Test
    @DisplayName("class arithmetic: kinds + channels, counted from the package itself")
    void theArithmetic() throws Exception {
        List<Class<?>> types = typesIn(Notification.class.getPackageName());

        long kinds = types.stream()
                .filter(Notification.class::isAssignableFrom)
                .filter(t -> t != Notification.class)
                .count();
        long channels = types.stream()
                .filter(NotificationChannel.class::isAssignableFrom)
                .filter(t -> t != NotificationChannel.class)
                .count();

        assertEquals(3, kinds, "refined abstractions");
        assertEquals(3, channels, "concrete implementors");

        // The slides' headline pair. m + n is what the solution replaces m x n with, and
        // the two roots are the overhead it charges for doing so.
        assertEquals(6, kinds + channels, "m + n, the classes that carry the two axes");
        assertEquals(9, kinds * channels, "m x n, the grid a class-per-pair design writes out");
        assertEquals(8, kinds + channels + 2, "every type on the design diagram");

        // And the two roots really are exactly two, so that last figure is not a guess.
        assertTrue(types.contains(Notification.class));
        assertTrue(types.contains(NotificationChannel.class));
    }

    @Test
    @DisplayName("a notification needs a channel")
    void nullChannelIsRejected() {
        assertThrows(NullPointerException.class, () -> new SimpleNotification(null));
    }
}
