package dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.*;

import java.util.List;

/** Runs the Bridge itself: three notification kinds over three channels, from six classes. */
public final class CombinationsMain {

    private CombinationsMain() {
    }

    public static void main(String[] args) {
        Console.heading("The Bridge - two hierarchies, one reference");
        Console.section("3 kinds x 3 channels, from 3 + 3 classes");
        TransportLog log = new TransportLog();
        Transports transports = new Transports(log);
        Recipient akin = Recipient.of("Akin", ChannelKind.EMAIL);

        List<NotificationChannel> channels = List.of(
                new EmailChannel(transports), new SmsChannel(transports), new PushChannel(transports));

        for (NotificationChannel channel : channels) {
            DeliveryResult simple = new SimpleNotification(channel).notify(akin, Console.shortMessage());
            DeliveryResult urgent = new UrgentNotification(channel).notify(akin, Console.shortMessage());
            DeliveryResult digest = new DigestNotification(channel)
                    .add(Console.shortMessage()).notify(akin, Console.shortMessage());
            System.out.printf("  %-6s simple=%s urgent=%s digest=%s%n",
                    channel.name(), simple.delivered(), urgent.delivered(), digest.delivered());
        }

        System.out.println("  " + log.sendCount() + " messages, and every combination worked");
        System.out.println("""

                  Notification + 3 refinements, NotificationChannel + 3 implementations.
                  A fourth channel costs one class and no notification kind is touched;
                  a fourth kind costs one class and no channel is touched.""");
    }
}
