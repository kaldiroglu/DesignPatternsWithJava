package dev.kaldiroglu.dp.structural.composite.hw.expression;

/**
 * Homework 2 — the expression tree.
 * <p>
 * {@code (3 + 4) * (10 - 2) / 4}, built as objects and evaluated by recursion.
 */
public class Main {

    public static void main(String[] args) {
        Expression expression = new Divide(
                new Multiply(
                        new Add(new Number(3), new Number(4)),
                        new Subtract(new Number(10), new Number(2))),
                new Number(4));

        System.out.println("expression : " + expression.toText());
        System.out.println("value      : " + expression.evaluate());
        System.out.println("nodes      : " + expression.nodeCount());

        System.out.println("""

                A leaf and an operation are the same type to the client, so a
                sub-expression can be swapped for a number and nothing above it
                notices:""");

        Expression simplified = new Divide(new Number(56), new Number(4));
        System.out.println("  " + simplified.toText() + " = " + simplified.evaluate());
        System.out.println("  same answer, 3 nodes instead of " + expression.nodeCount());

        System.out.println("\nAnd a node that cannot answer says so, loudly:");
        try {
            new Divide(new Number(1), new Subtract(new Number(5), new Number(5))).evaluate();
        } catch (ArithmeticException e) {
            System.out.println("  " + e.getMessage());
        }
    }
}
