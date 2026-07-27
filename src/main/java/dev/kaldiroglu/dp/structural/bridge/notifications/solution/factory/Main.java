package dev.kaldiroglu.dp.structural.bridge.notifications.solution.factory;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.ChannelKind;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Console;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.DeliveryResult;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.TransportLog;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Transports;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.EmailChannel;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.SimpleNotification;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.UrgentNotification;

import java.util.List;

/**
 * Runs the first variation: a factory decides which implementor a recipient gets.
 * <p>
 * GoF's implementation issue 2 (p. 155): "How, where, and when do you decide which
 * Implementor class to instantiate?"
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Console.heading("Variation 1 - the factory chooses the implementor");
        byPreference();
        kindAndChannelAreIndependent();
        reRouting();
    }

    private static void byPreference() {
        Console.section("each recipient is reached the way they asked to be");
        TransportLog log = new TransportLog();
        var service = new NotificationService(new PreferenceChannelFactory(new Transports(log)));

        List<Recipient> people = List.of(
                Recipient.of("Akin", ChannelKind.EMAIL),
                Recipient.of("Bora", ChannelKind.SMS),
                Recipient.of("Ceyda", ChannelKind.PUSH));

        for (Recipient person : people) {
            DeliveryResult r = service.send(UrgentNotification::new, person, Console.shortMessage());
            System.out.printf("  %-6s prefers %-5s -> sent over %s%n",
                    person.name(), person.preferred(), r.channel());
        }

        System.out.println("""

                  One notification kind, one call site, three channels - chosen from a
                  value that came out of a database while the program was running.

                  This is the demo that cannot be written at all against the design in
                  the problem package, where Bora was sent an email.""");
    }

    private static void kindAndChannelAreIndependent() {
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

    private static void reRouting() {
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
