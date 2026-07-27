package dev.kaldiroglu.dp.structural.bridge.violation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * The violation is silent by nature, so it can only be caught by capturing what a caller
 * holding the supertype actually sees. That is what these tests do.
 */
class ViolationTest {

    private static String outputOf(Consumer<AType> action, AType subject) {
        PrintStream original = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));
        try {
            action.accept(subject);
        } finally {
            System.setOut(original);
        }
        return captured.toString();
    }

    @Test
    @DisplayName("the supertype prints, which is the contract")
    void theSupertypeKeepsItsPromise() {
        assertEquals("My variable: 42" + System.lineSeparator(),
                outputOf(AType::doIt, new AType(42, true)));
    }

    @Test
    @DisplayName("the subtype prints nothing, and says nothing about it")
    void theSubtypeBreaksItSilently() {
        String output = outputOf(AType::doIt, new ASubType(42, true));

        assertEquals("", output);
        assertNotEquals(outputOf(AType::doIt, new AType(42, true)), output);
    }

    @Test
    @DisplayName("a caller holding AType cannot tell, except by testing the type")
    void substitutabilityIsBroken() {
        AType[] both = {new AType(42, true), new ASubType(42, true)};

        long printed = 0;
        for (AType each : both) {
            if (!outputOf(AType::doIt, each).isEmpty()) {
                printed++;
            }
        }
        // Two objects of the declared type, one of them silent. No exception, no signal.
        assertEquals(1, printed);
    }

    @Test
    @DisplayName("and the stored string is null until doIt has run")
    void theSecondBrokenPromise() {
        ASubType early = new ASubType(42, true);
        assertNull(early.getAStringVariable());

        early.doIt();
        assertEquals("My variable: 42", early.getAStringVariable());
    }

    @Test
    @DisplayName("the fix is delegation, not a better override")
    void delegationCannotDoThis() {
        // A refinement in bridge.basic.pattern owns its own doIt() and merely calls the
        // implementation. Whatever the implementation does, the refinement's own contract
        // is still executed — there is no override to break it.
        assertTrue(dev.kaldiroglu.dp.structural.bridge.basic.pattern.AnAbstraction.class
                .isAssignableFrom(
                        dev.kaldiroglu.dp.structural.bridge.basic.pattern.ASubAbstraction.class));
        assertEquals(1,
                dev.kaldiroglu.dp.structural.bridge.basic.pattern.ASubAbstraction.class
                        .getDeclaredFields().length);
    }
}
