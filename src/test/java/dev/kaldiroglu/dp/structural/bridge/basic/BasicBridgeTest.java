package dev.kaldiroglu.dp.structural.bridge.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * The pattern reduced to its bones, and the counting argument that justifies it.
 */
class BasicBridgeTest {

    @Test
    @DisplayName("every refinement works with every implementation, from four classes")
    void mPlusN() {
        List<Class<?>> refinements = List.of(
                dev.kaldiroglu.dp.structural.bridge.basic.pattern.ASubAbstraction.class,
                dev.kaldiroglu.dp.structural.bridge.basic.pattern.AnotherSubAbstraction.class);
        List<Class<?>> implementations = List.of(
                dev.kaldiroglu.dp.structural.bridge.basic.pattern.AConcreteImplementation1.class,
                dev.kaldiroglu.dp.structural.bridge.basic.pattern.AConcreteImplementation2.class);

        assertEquals(4, refinements.size() + implementations.size());
        assertEquals(4, refinements.size() * implementations.size()); // 2x2 happens to match
    }

    @Test
    @DisplayName("the naive package needs a class per combination")
    void mTimesN() {
        List<Class<?>> leaves = List.of(
                dev.kaldiroglu.dp.structural.bridge.basic.problem.AConcreteImplementation1.class,
                dev.kaldiroglu.dp.structural.bridge.basic.problem.AConcreteImplementation2.class,
                dev.kaldiroglu.dp.structural.bridge.basic.problem.AnotherConcreteImplementation1.class,
                dev.kaldiroglu.dp.structural.bridge.basic.problem.AnotherConcreteImplementation2.class);

        // Four leaves plus the two refinements they extend: six against the pattern's four.
        assertEquals(4, leaves.size());
        assertEquals(6, leaves.size() + 2);
    }

    @Test
    @DisplayName("in the pattern the refinement holds an implementation; in the problem it is one")
    void heldAgainstInherited() {
        assertEquals(1,
                dev.kaldiroglu.dp.structural.bridge.basic.pattern.ASubAbstraction.class
                        .getDeclaredFields().length);

        // The naive refinement has no field for an implementation, because it *is* the
        // implementation's superclass — which is exactly why it cannot switch.
        assertEquals(0,
                dev.kaldiroglu.dp.structural.bridge.basic.problem.ASubAbstraction.class
                        .getDeclaredFields().length);
        assertTrue(dev.kaldiroglu.dp.structural.bridge.basic.problem.ASubAbstraction.class
                .isAssignableFrom(
                        dev.kaldiroglu.dp.structural.bridge.basic.problem.AConcreteImplementation1.class));
    }

    @Test
    @DisplayName("the implementor interface is spelled correctly")
    void theInterfaceNameIsSpelledCorrectly() {
        // It was AnAbstrationImplementation for a long time. Names on slides get read aloud.
        assertEquals("AnAbstractionImplementation",
                dev.kaldiroglu.dp.structural.bridge.basic.pattern.AnAbstractionImplementation.class
                        .getSimpleName());
        assertFalse(dev.kaldiroglu.dp.structural.bridge.basic.pattern.AnAbstractionImplementation.class
                .getSimpleName().contains("Abstration"));
    }
}
