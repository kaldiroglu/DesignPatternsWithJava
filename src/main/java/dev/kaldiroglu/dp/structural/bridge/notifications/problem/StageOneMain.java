package dev.kaldiroglu.dp.structural.bridge.notifications.problem;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.*;

/**
 * Stage one: one class, with a switch on each axis. Two sends work, and nine branches pay for
 * them. {@link ForgottenRuleMain} shows the send that fails.
 */
public final class StageOneMain {

    private StageOneMain() {
    }

    public static void main(String[] args) {
        Console.heading("Two axes, and three designs that tie them together");
        Console.section("1. one class, a switch on each axis");
        TransportLog log = new TransportLog();
        SwitchingNotifier notifier = new SwitchingNotifier(new Transports(log));
        Recipient akin = Recipient.of("Akin", ChannelKind.EMAIL);

        notifier.send(SwitchingNotifier.Kind.SIMPLE, ChannelKind.EMAIL, akin, Console.shortMessage());
        notifier.send(SwitchingNotifier.Kind.URGENT, ChannelKind.SMS, akin, Console.shortMessage());
        System.out.println("  works: " + log.sendCount() + " messages on the wire");
        System.out.println("  but 3 kinds x 3 channels = 9 branches, in one method");
    }
}
