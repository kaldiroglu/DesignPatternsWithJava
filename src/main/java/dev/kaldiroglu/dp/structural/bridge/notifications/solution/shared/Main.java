package dev.kaldiroglu.dp.structural.bridge.notifications.solution.shared;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.ChannelKind;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Console;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Recipient;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.TransportLog;
import dev.kaldiroglu.dp.structural.bridge.notifications.domain.Transports;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.DigestNotification;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.EmailChannel;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.Notification;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.SimpleNotification;
import dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.UrgentNotification;

import java.util.List;

/**
 * Runs the second variation: one implementor object serving several abstractions.
 * <p>
 * GoF's implementation issue 3 (p. 156), which they answer in C++ with reference counting
 * inside a Handle/Body pair.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Console.heading("Variation 2 - one implementor, shared");

        Console.section("three notifications, one channel object");
        TransportLog log = new TransportLog();
        var pooled = new PooledChannel(new EmailChannel(new Transports(log)));
        Recipient akin = Recipient.of("Akin", ChannelKind.EMAIL);

        List<Notification> notifications = List.of(
                new SimpleNotification(pooled.acquire()),
                new UrgentNotification(pooled.acquire()),
                new DigestNotification(pooled.acquire()));

        notifications.forEach(n -> n.notify(akin, Console.shortMessage()));

        System.out.println("  abstractions sharing the channel : " + pooled.users());
        System.out.println("  messages through the one channel : " + pooled.messagesSent());
        System.out.println("  transport connections opened     : " + log.connectionsOpened());

        System.out.println("""

                  A channel is expensive - an SMTP connection, an HTTP client, a vendor
                  session - and nothing in the pattern says each abstraction needs its
                  own. The sharing is invisible to whoever holds the notification, which
                  is GoF's third consequence.

                  What it costs: the implementor now holds shared state. It has to be
                  thread-safe, its failures belong to everybody using it at once, and it
                  cannot hold anything specific to one abstraction.""");
    }
}
