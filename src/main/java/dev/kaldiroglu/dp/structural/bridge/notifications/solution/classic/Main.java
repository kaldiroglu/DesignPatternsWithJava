package dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.ChannelKind;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Console;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.DeliveryResult;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.TransportLog;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Transports;

import java.util.List;

/**
 * Runs the Bridge itself: three notification kinds over three channels, from six classes.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Console.heading("The Bridge - two hierarchies, one reference");
        everyCombination();
        theChannelsRules();
        runTimeSwap();
    }

    private static void everyCombination() {
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

    private static void theChannelsRules() {
        Console.section("the channel's rules are asked for, never assumed");
        TransportLog log = new TransportLog();
        Transports transports = new Transports(log);
        Recipient akin = Recipient.of("Akin", ChannelKind.EMAIL);
        var message = Console.longMessage();

        DeliveryResult onEmail = new UrgentNotification(new EmailChannel(transports)).notify(akin, message);
        DeliveryResult onSms = new UrgentNotification(new SmsChannel(transports)).notify(akin, message);
        DeliveryResult onPush = new UrgentNotification(new PushChannel(transports)).notify(akin, message);

        System.out.println("  one UrgentNotification class, one long message:");
        System.out.println("    over email : " + onEmail.bodySent().length() + " characters");
        System.out.println("    over sms   : " + onSms.bodySent().length()
                + " characters  (truncated: " + onSms.truncated(message.body()) + ")");
        System.out.println("    over push  : " + onPush.bodySent().length() + " characters");
        System.out.println("""

                  The notification asked maxBodyLength(). It never asked which channel
                  it was holding - and there is no instanceof anywhere in this package.

                  The digest gets the same treatment for free, which is why the bug that
                  threw in the problem package cannot happen here.""");
    }

    private static void runTimeSwap() {
        Console.section("the channel can change on a notification that already exists");
        TransportLog log = new TransportLog();
        Transports transports = new Transports(log);
        Recipient akin = Recipient.of("Akin", ChannelKind.EMAIL);

        Notification notification = new UrgentNotification(new EmailChannel(transports));
        System.out.println("  channel            : " + notification.channelName());
        notification.notify(akin, Console.shortMessage());

        notification.setChannel(new SmsChannel(transports));
        System.out.println("  after setChannel   : " + notification.channelName() + "   (the same object)");
        notification.notify(akin, Console.shortMessage());

        System.out.println("  on the wire        : " + log.sendCount("email")
                + " email, " + log.sendCount("sms") + " sms");
    }
}
