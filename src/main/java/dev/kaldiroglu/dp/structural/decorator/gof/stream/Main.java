package dev.kaldiroglu.dp.structural.decorator.gof.stream;

/**
 * The Sample Code example of GoF pp. 182–184, run both ways.
 * <p>
 * {@code problem.Main} and {@code solution.Main} each run one design on its own. This class
 * runs them in order.
 */
public final class Main {

    private Main() {
    }

    public static void run() {
        System.out.println("=".repeat(72));
        System.out.println("GoF Sample Code (pp. 182–184) — compressing and encoding a stream");
        System.out.println("=".repeat(72));
        System.out.println("\npayload: " + Payload.TEXT);

        System.out.println();
        dev.kaldiroglu.dp.structural.decorator.gof.stream.problem.Main.main(new String[0]);

        System.out.println();
        dev.kaldiroglu.dp.structural.decorator.gof.stream.solution.Main.main(new String[0]);
    }

    public static void main(String[] args) {
        run();
    }
}
