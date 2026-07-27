package dev.kaldiroglu.dp.structural.bridge.basic.problem;

/** A second refinement — which is why this package now needs four leaf classes. */
public class AnotherSubAbstraction implements AnAbstraction {

    @Override
    public void doIt() {
        System.out.println("AnotherSubAbstraction: I am the second refinement, and I do nothing.");
    }
}
