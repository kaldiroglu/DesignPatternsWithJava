package dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.*;

/** The channel changes on a notification that already exists. */
public final class SwapMain {

    private SwapMain() {
    }

    public static void main(String[] args) {
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
