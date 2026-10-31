package dev.kaldiroglu.dp.structural.bridge.notifications.solution.factory;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.*;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.EmailChannel;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.UrgentNotification;

/** When a vendor goes down, one line of configuration moves its traffic. */
public final class ReRouteMain {

    private ReRouteMain() {
    }

    public static void main(String[] args) {
        Console.section("when a vendor goes down");
        TransportLog log = new TransportLog();
        Transports transports = new Transports(log);
        var factory = new PreferenceChannelFactory(transports);

        factory.register(ChannelKind.PUSH, new EmailChannel(transports));
        var service = new NotificationService(factory);

        DeliveryResult r = service.send(UrgentNotification::new,
                Recipient.of("Ceyda", ChannelKind.PUSH), Console.shortMessage());

        System.out.println("  Ceyda prefers PUSH, push is re-pointed at email -> " + r.channel());
        System.out.println("  one line of configuration, and no notification kind was touched");
    }
}
