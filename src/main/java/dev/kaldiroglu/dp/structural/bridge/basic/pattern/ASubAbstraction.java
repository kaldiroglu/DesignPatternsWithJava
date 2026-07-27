package dev.kaldiroglu.dp.structural.bridge.basic.pattern;

import java.util.Objects;

/** A RefinedAbstraction: it holds an implementation and never asks which one. */
public class ASubAbstraction implements AnAbstraction {

    private final AnAbstractionImplementation implementation;

    public ASubAbstraction(AnAbstractionImplementation implementation) {
        this.implementation = Objects.requireNonNull(implementation);
    }

    @Override
    public void doIt() {
        System.out.println("ASubAbstraction: I am the first refinement.");
        implementation.doingIt();
    }
}
