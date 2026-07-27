package dev.kaldiroglu.dp.structural.bridge.basic.pattern;

/** A ConcreteImplementor. */
public class AConcreteImplementation1 implements AnAbstractionImplementation {

    @Override
    public void doingIt() {
        System.out.println("  AConcreteImplementation1: I am doing it!");
    }
}
