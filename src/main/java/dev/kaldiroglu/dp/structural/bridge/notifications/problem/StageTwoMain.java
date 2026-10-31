package dev.kaldiroglu.dp.structural.bridge.notifications.problem;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.*;

/** Stage two: a class for every (kind, channel) pair. */
public final class StageTwoMain {

    private StageTwoMain() {
    }

    public static void main(String[] args) {
        Console.section("2. a class for every (kind, channel) pair");
        TransportLog log = new TransportLog();
        Transports transports = new Transports(log);
        Recipient akin = Recipient.of("Akin", ChannelKind.EMAIL);

        new UrgentEmailNotification(transports).send(akin, Console.shortMessage());
        new UrgentSmsNotification(transports).send(akin, Console.shortMessage());

        System.out.println("  works: " + log.sendCount() + " messages, 2 classes");
        System.out.println("""
                  The retry loop - the only thing 'urgent' means - is written twice, in
                  two classes that cannot share it, and will be written a third time
                  when push arrives.

                  3 kinds x 3 channels = 9 classes
                  a fourth channel     = 3 more
                  a fourth kind        = 3 more""");
    }
}
