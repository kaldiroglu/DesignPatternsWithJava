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
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.Notification;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.NotificationChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    @DisplayName("switch: three branches send an SMS and two state the rule, so one forgot")
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

    /**
     * The counts the slides quote about this class, read from the class itself.
     * <p>
     * These are claims about the <em>source text</em> — how many branches there are, and
     * how many of them remember a rule — so the source is what the test reads. Asserting
     * {@code 9 == 3 * 3} would prove something about integers and would go on passing
     * after somebody added a branch or deleted a limit.
     */
    @Test
    @DisplayName("switch: nine branches by hand, and the SMS rule written in two of three")
    void branchesMultiply() throws Exception {
        String source = Files.readString(Path.of(
                "src/main/java/dev/kaldiroglu/dp/structural/bridge/notifications"
                + "/problem/SwitchingNotifier.java"));
        String body = source.substring(source.indexOf("public final class"));

        // Twelve case labels in all: three for the kind, then three per kind for the
        // channel. The nine the slides count are the leaves — the (kind, channel) pairs.
        assertEquals(12, countOf(body, "case "), "case labels in the whole class");
        int leaves = countOf(body, "case EMAIL ->")
                   + countOf(body, "case SMS ->")
                   + countOf(body, "case PUSH ->");
        assertEquals(9, leaves, "branches, one per pair, written by hand");

        assertEquals(3, countOf(body, "case SMS ->"), "branches that send an SMS");
        assertEquals(2, countOf(body, "Transports.SMS_LIMIT"), "of them state the limit");

        // The push limit, by contrast, is remembered in all three of its branches. The
        // design does not fail reliably; it fails wherever somebody happened to forget.
        assertEquals(3, countOf(body, "case PUSH ->"), "branches that send a push");
        assertEquals(3, countOf(body, "Transports.PUSH_LIMIT"), "all of them state the limit");
    }

    private static int countOf(String text, String needle) {
        int count = 0;
        for (int i = text.indexOf(needle); i >= 0; i = text.indexOf(needle, i + needle.length())) {
            count++;
        }
        return count;
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

    /**
     * The product the slides quote, with the two axes counted from the bridge package rather
     * than written here as literals.
     * <p>
     * This package deliberately writes only three of the nine pair classes, so the nine
     * cannot be counted directly — but the two axes can, and the product follows from them.
     * The version this replaced declared {@code int kinds = 3, channels = 3} and then
     * asserted {@code 9 == kinds * channels}, which is true of the integers whatever the
     * design contains.
     */
    @Test
    @DisplayName("class per pair: the class count is the product of the two axes")
    void classesMultiply() throws Exception {
        long kinds = countIn(Notification.class.getPackageName(), Notification.class);
        long channels = countIn(NotificationChannel.class.getPackageName(),
                NotificationChannel.class);

        assertEquals(3, kinds, "notification kinds");
        assertEquals(3, channels, "channels");

        assertEquals(9, kinds * channels, "classes a design with one per pair must write");
        assertEquals(12, kinds * (channels + 1), "a fourth channel: three more");
        assertEquals(12, (kinds + 1) * channels, "a fourth kind: three more");

        // And the same two axes cost kinds + channels in the bridge design.
        assertEquals(6, kinds + channels, "m + n, next door in solution.classic");
    }

    /** Concrete implementations of {@code root} declared in {@code packageName}. */
    private static long countIn(String packageName, Class<?> root) throws Exception {
        List<URL> roots = java.util.Collections.list(ProblemTest.class.getClassLoader()
                .getResources(packageName.replace('.', '/')));
        assertFalse(roots.isEmpty(), "package not on the test classpath: " + packageName);
        java.util.Set<Class<?>> found = new java.util.LinkedHashSet<>();
        for (URL url : roots) {
            try (java.util.stream.Stream<Path> files = Files.list(Path.of(url.toURI()))) {
                for (Path file : files.sorted().toList()) {
                    String name = file.getFileName().toString();
                    if (!name.endsWith(".class") || name.contains("$")) {
                        continue;
                    }
                    Class<?> type = Class.forName(
                            packageName + '.' + name.substring(0, name.length() - 6));
                    if (root.isAssignableFrom(type) && type != root
                            && !java.lang.reflect.Modifier.isAbstract(type.getModifiers())) {
                        found.add(type);
                    }
                }
            }
        }
        return found.size();
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
