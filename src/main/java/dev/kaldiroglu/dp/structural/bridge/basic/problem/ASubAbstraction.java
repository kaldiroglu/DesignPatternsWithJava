package dev.kaldiroglu.dp.structural.bridge.basic.problem;

/** A refinement. It cannot do anything on its own — the implementation is a subclass. */
public class ASubAbstraction implements AnAbstraction {

    @Override
    public void doIt() {
        System.out.println("ASubAbstraction: I am the first refinement, and I do nothing.");
    }
}
