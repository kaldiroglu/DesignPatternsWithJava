package dev.kaldiroglu.dp.structural.bridge.notifications.solution.factory;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.*;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.SimpleNotification;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.UrgentNotification;

/** The kind and the channel are chosen separately. */
public final class KindAndChannelMain {

    private KindAndChannelMain() {
    }

    public static void main(String[] args) {
        Console.section("the kind and the channel are chosen separately");
        TransportLog log = new TransportLog();
        var service = new NotificationService(new PreferenceChannelFactory(new Transports(log)));
        Recipient bora = Recipient.of("Bora", ChannelKind.SMS);

        System.out.println("  simple -> " + service.send(SimpleNotification::new, bora,
                Console.shortMessage()).channel());
        System.out.println("  urgent -> " + service.send(UrgentNotification::new, bora,
                Console.shortMessage()).channel());
        System.out.println("  the caller named neither a channel class nor a combination class");
    }
}
