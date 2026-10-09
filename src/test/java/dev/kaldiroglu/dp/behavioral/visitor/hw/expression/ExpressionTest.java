package dev.kaldiroglu.dp.behavioral.visitor.hw.expression;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Homework three: the expression (2 + 3) * -4, evaluated, printed and measured. */
class ExpressionTest {

    private static Expr expression() {
        return new Mul(new Add(new Num(2), new Num(3)), new Neg(new Num(4)));
    }

    @Test
    @DisplayName("(2 + 3) * -4 evaluates to -20")
    void evaluate() {
        assertEquals(-20, expression().accept(new Evaluator()));
    }

    @Test
    @DisplayName("the tree has a depth of 3")
    void depth() {
        assertEquals(3, expression().accept(new DepthCounter()));
    }

    @Test
    @DisplayName("the printer writes the expression back with brackets")
    void print() {
        assertEquals("((2 + 3) * -4)", expression().accept(new Printer()));
    }

    @Test
    @DisplayName("the visitor has one visit method for each of the four node classes")
    void fourVisitMethods() {
        assertEquals(4, ExprVisitor.class.getDeclaredMethods().length);
    }
}
