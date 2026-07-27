package dev.kaldiroglu.dp.structural.bridge.notifications.problem;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.ChannelKind;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Console;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.DeliveryResult;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Message;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.TransportLog;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Transports;

/**
 * Runs the three naive designs.
 * <p>
 * All three work. The output is about what each one costs, and the third section is the
 * one worth waiting for.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Console.heading("Two axes, and three ways of welding them together");
        switching();
        classPerPair();
        inheritTheChannel();
    }

    private static void switching() {
        Console.section("1. one class, a switch on each axis");
        TransportLog log = new TransportLog();
        SwitchingNotifier notifier = new SwitchingNotifier(new Transports(log));
        Recipient akin = Recipient.of("Akin", ChannelKind.EMAIL);

        notifier.send(SwitchingNotifier.Kind.SIMPLE, ChannelKind.EMAIL, akin, Console.shortMessage());
        notifier.send(SwitchingNotifier.Kind.URGENT, ChannelKind.SMS, akin, Console.shortMessage());
        System.out.println("  works: " + log.sendCount() + " messages on the wire");
        System.out.println("  but 3 kinds x 3 channels = 9 branches, in one method");

        System.out.println("\n  the 160-character rule is stated in four places, so:");
        try {
            notifier.send(SwitchingNotifier.Kind.DIGEST, ChannelKind.SMS, akin, Console.longMessage());
            System.out.println("  (no error)");
        } catch (IllegalArgumentException e) {
            System.out.println("  " + e.getClass().getSimpleName() + ": " + e.getMessage());
            System.out.println("  nobody removed the rule from sendDigest - it was never added");
        }
    }

    private static void classPerPair() {
        Console.section("2. a class for every (kind, channel) pair");
        TransportLog log = new TransportLog();
        Transports transports = new Transports(log);
        Recipient akin = Recipient.of("Akin", ChannelKind.EMAIL);

        new UrgentEmailNotification(transports).send(akin, Console.shortMessage());
        new UrgentSmsNotification(transports).send(akin, Console.shortMessage());

        System.out.println("  works: " + log.sendCount() + " messages, 2 classes");
        System.out.println("""
                  The retry loop - the only thing 'urgent' means - is written twice, in
                  two classes that cannot share it, and will be written a third time
                  when push arrives.

                  3 kinds x 3 channels = 9 classes
                  a fourth channel     = 3 more
                  a fourth kind        = 3 more""");
    }

    private static void inheritTheChannel() {
        Console.section("3. the notification inherits the channel");
        TransportLog log = new TransportLog();
        var urgent = new EmailBoundUrgentNotification(new Transports(log));
        Recipient bora = Recipient.of("Bora", ChannelKind.SMS);

        DeliveryResult result = urgent.send(bora, Console.shortMessage());

        System.out.println("  Bora's stored preference : " + bora.preferred());
        System.out.println("  the notification used    : " + result.channel());
        System.out.println("  delivered                : " + result.delivered());
        System.out.println("""

                  Nothing failed. Bora simply did not get what he asked for, and the
                  result says the send succeeded.

                  EmailBoundUrgentNotification is an EmailSender - not 'has a channel'
                  but is one - so a preference read from a database at run time cannot
                  reach it. A superclass is chosen when the code is compiled.""");
    }
}
