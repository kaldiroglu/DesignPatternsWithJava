package dev.kaldiroglu.dp.structural.composite.hw.expression;

/**
 * The Component: anything that can be evaluated.
 * <p>
 * This example is worth doing because the tree <em>is</em> the data. In the org chart the
 * hierarchy models something that exists in the world; here the hierarchy <em>is</em> the
 * arithmetic, and the recursion in {@link #evaluate()} is the evaluation.
 */
public interface Expression {

    double evaluate();

    /** Fully parenthesized, so the shape of the tree is visible in the text. */
    String toText();

    /** How many numbers and operations this expression is made of. */
    int nodeCount();
}
