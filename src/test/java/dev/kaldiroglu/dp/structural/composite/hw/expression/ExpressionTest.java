package dev.kaldiroglu.dp.structural.composite.hw.expression;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Here the tree is the data, so evaluating it is walking it. */
class ExpressionTest {

    private static Expression sample() {
        return new Divide(
                new Multiply(new Add(new Number(3), new Number(4)),
                             new Subtract(new Number(10), new Number(2))),
                new Number(4));
    }

    @Test
    @DisplayName("evaluation is the recursion")
    void evaluate() {
        assertEquals(14.0, sample().evaluate(), 1e-9);
        assertEquals(7.0, new Add(new Number(3), new Number(4)).evaluate(), 1e-9);
        assertEquals(3.0, new Number(3).evaluate(), 1e-9);
    }

    @Test
    @DisplayName("the text shows the shape of the tree")
    void text() {
        assertEquals("(((3 + 4) * (10 - 2)) / 4)", sample().toText());
        assertEquals("3", new Number(3).toText());
    }

    @Test
    @DisplayName("a leaf is one node; an operation is itself plus both sides")
    void nodeCount() {
        assertEquals(1, new Number(1).nodeCount());
        assertEquals(3, new Add(new Number(1), new Number(2)).nodeCount());
        assertEquals(9, sample().nodeCount());
    }

    @Test
    @DisplayName("a sub-expression can be replaced by a number and nothing above notices")
    void substitutability() {
        Expression full = sample();
        Expression simplified = new Divide(new Number(56), new Number(4));

        assertEquals(full.evaluate(), simplified.evaluate(), 1e-9);
        assertEquals(3, simplified.nodeCount());
    }

    @Test
    @DisplayName("a composite may hold exactly two children and still be a composite")
    void twoChildrenIsEnough() {
        // Composite does not require an unbounded collection — it requires components.
        assertEquals(2, BinaryOperation.class.getDeclaredFields().length);
    }

    @Test
    @DisplayName("a node that cannot answer refuses rather than inventing a number")
    void divisionByZero() {
        ArithmeticException thrown = assertThrows(ArithmeticException.class,
                () -> new Divide(new Number(1), new Subtract(new Number(5), new Number(5))).evaluate());
        assertEquals("division by zero in (1 / (5 - 5))", thrown.getMessage());
    }
}
