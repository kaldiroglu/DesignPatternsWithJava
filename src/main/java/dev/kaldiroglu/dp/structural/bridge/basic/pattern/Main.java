package dev.kaldiroglu.dp.structural.bridge.basic.pattern;

/** Every abstraction over every implementation, from four classes. */
public class Main {

    public static void main(String[] args) {
        AnAbstractionImplementation[] implementations = {
                new AConcreteImplementation1(), new AConcreteImplementation2()};

        for (AnAbstractionImplementation implementation : implementations) {
            new Client(new ASubAbstraction(implementation)).start();
            new Client(new AnotherSubAbstraction(implementation)).start();
        }

        System.out.println();
        System.out.println("2 abstractions + 2 implementations = 4 classes, 4 combinations.");
        System.out.println("basic.problem needs 6 classes for the same four combinations.");
    }
}
