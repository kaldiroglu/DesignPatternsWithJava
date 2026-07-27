package dev.kaldiroglu.dp.structural.bridge.basic.pattern;

import java.util.Objects;

/**
 * A second RefinedAbstraction — the class that makes the counting argument visible.
 * <p>
 * It cost <strong>one</strong> class and works with every implementation that exists or ever
 * will. Its counterpart in {@code basic.problem} cost one class <em>per implementation</em>.
 */
public class AnotherSubAbstraction implements AnAbstraction {

    private final AnAbstractionImplementation implementation;

    public AnotherSubAbstraction(AnAbstractionImplementation implementation) {
        this.implementation = Objects.requireNonNull(implementation);
    }

    @Override
    public void doIt() {
        System.out.println("AnotherSubAbstraction: I am the second refinement.");
        implementation.doingIt();
        implementation.doingIt(); // a refinement may compose the primitive more than once
    }
}
