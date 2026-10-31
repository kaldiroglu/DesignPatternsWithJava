package dev.kaldiroglu.dp.structural.decorator.gof.visual;

/**
 * The Motivation example of GoF p. 175, run both ways: first by subclassing, then by
 * decorating. The pictures are identical; the designs are not.
 * <p>
 * {@code problem.Main} and {@code solution.Main} each run one design on its own. This class
 * runs them in order.
 */
public final class Main {

    private Main() {
    }

    public static void run() {
        System.out.println("=".repeat(72));
        System.out.println("GoF Motivation (p. 175) — a TextView with a border and a scrollbar");
        System.out.println("=".repeat(72));

        System.out.println();
        dev.kaldiroglu.dp.structural.decorator.gof.visual.problem.Main.main(new String[0]);

        System.out.println();
        dev.kaldiroglu.dp.structural.decorator.gof.visual.solution.Main.main(new String[0]);
    }

    public static void main(String[] args) {
        run();
    }
}
