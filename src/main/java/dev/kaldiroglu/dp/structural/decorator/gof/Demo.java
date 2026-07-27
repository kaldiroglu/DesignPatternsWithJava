package dev.kaldiroglu.dp.structural.decorator.gof;

/** Runs both GoF Decorator examples, each as a problem and then as a solution. */
public final class Demo {

    private Demo() {
    }

    public static void main(String[] args) {
        dev.kaldiroglu.dp.structural.decorator.gof.visual.Demo.run();
        System.out.println();
        dev.kaldiroglu.dp.structural.decorator.gof.stream.Demo.run();
    }
}
