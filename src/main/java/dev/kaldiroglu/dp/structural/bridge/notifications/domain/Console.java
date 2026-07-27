package dev.kaldiroglu.dp.structural.bridge.notifications.domain;

/** Section headings for the demos, so each Main stays about the design rather than layout. */
public final class Console {

    private Console() {
    }

    public static void heading(String title) {
        System.out.println("=".repeat(72));
        System.out.println(title);
        System.out.println("=".repeat(72));
    }

    public static void section(String title) {
        System.out.println("\n--- " + title + " " + "-".repeat(Math.max(0, 68 - title.length())));
    }

    /** The message every demo sends, so their outputs can be compared. */
    public static Message shortMessage() {
        return new Message("Order 4021 shipped", "It is on its way.");
    }

    /** Long enough that the channels disagree about what to do with it. */
    public static Message longMessage() {
        return new Message("Payment failed",
                "We could not take payment for order 4021. Please update the card on file. "
                + "If the payment is not completed within 48 hours the order will be released "
                + "and the reserved stock returned to the warehouse for other customers.");
    }
}
