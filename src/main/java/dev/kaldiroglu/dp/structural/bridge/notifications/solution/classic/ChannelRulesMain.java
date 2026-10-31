package dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.*;

/** One long message through one UrgentNotification class: each channel sets the length. */
public final class ChannelRulesMain {

    private ChannelRulesMain() {
    }

    public static void main(String[] args) {
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
}
