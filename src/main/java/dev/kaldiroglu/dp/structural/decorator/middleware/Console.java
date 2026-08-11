package dev.kaldiroglu.dp.structural.decorator.middleware;

/**
 * The headings the demos print. Extracted so that each example's own Main can be run on
 * its own and still look like part of the same programme.
 */
public final class Console {

    private Console() {
    }

    public static void heading(String title) {
        System.out.println("\n" + "=".repeat(72));
        System.out.println(title);
        System.out.println("=".repeat(72));
    }

    public static void section(String title) {
        System.out.println("\n--- " + title + " "
                + "-".repeat(Math.max(0, 68 - title.length())));
    }
}
