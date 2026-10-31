package dev.kaldiroglu.dp.structural.bridge.notifications.problem;

import dev.kaldiroglu.dp.structural.bridge.notifications.domain.*;

/** Stage one's failing request: a long digest over SMS, in the branch without the limit. */
public final class ForgottenRuleMain {

    private ForgottenRuleMain() {
    }

    public static void main(String[] args) {
        Console.section("1. one class, a switch on each axis");
        SwitchingNotifier notifier = new SwitchingNotifier(new Transports(new TransportLog()));
        Recipient akin = Recipient.of("Akin", ChannelKind.EMAIL);

        System.out.println("\n  the 160-character rule is stated in four places, so:");
        try {
            notifier.send(SwitchingNotifier.Kind.DIGEST, ChannelKind.SMS, akin, Console.longMessage());
            System.out.println("  (no error)");
        } catch (IllegalArgumentException e) {
            System.out.println("  " + e.getClass().getSimpleName() + ": " + e.getMessage());
            System.out.println("  nobody removed the rule from sendDigest - it was never added");
        }
    }
}
