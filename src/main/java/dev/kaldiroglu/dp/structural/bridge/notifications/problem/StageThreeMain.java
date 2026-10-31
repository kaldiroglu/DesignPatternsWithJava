package dev.kaldiroglu.dp.structural.bridge.notifications.problem;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.*;

/** Stage three: the notification inherits the channel, and Bora's preference cannot reach it. */
public final class StageThreeMain {

    private StageThreeMain() {
    }

    public static void main(String[] args) {
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
