package dev.kaldiroglu.dp.structural.bridge.gof.window;

/** Prints two rendered windows next to each other, so the platforms can be compared. */
public final class Display {

    private Display() {
    }

    public static void sideBySide(String left, String right) {
        String[] l = left.split("\n");
        String[] r = right.split("\n");
        int width = 0;
        for (String line : l) {
            width = Math.max(width, line.length());
        }
        for (int i = 0; i < Math.max(l.length, r.length); i++) {
            String a = i < l.length ? l[i] : "";
            String b = i < r.length ? r[i] : "";
            System.out.printf("%-" + (width + 5) + "s%s%n", a, b);
        }
    }

    public static void heading(String title) {
        System.out.println("=".repeat(72));
        System.out.println(title);
        System.out.println("=".repeat(72));
    }

    public static void section(String title) {
        System.out.println("\n--- " + title + " " + "-".repeat(Math.max(0, 68 - title.length())));
    }
}
