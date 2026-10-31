package dev.kaldiroglu.dp.structural.bridge.notifications.solution.factory;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.*;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.UrgentNotification;

import java.util.List;

/**
 * Runs the first variation: a factory decides which implementor a recipient gets.
 * <p>
 * GoF's implementation issue 2 (p. 155): "How, where, and when do you decide which
 * Implementor class to instantiate?"
 */
public final class PreferenceMain {

    private PreferenceMain() {
    }

    public static void main(String[] args) {
        Console.heading("Variation 1 - the factory chooses the implementor");
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
}
