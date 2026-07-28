package dev.kaldiroglu.dp.structural.composite.hw.expression;

import java.util.Objects;

/**
 * The Composite: an operation over two sub-expressions.
 * <p>
 * Note that it holds exactly two children rather than a list. A Composite does not have to
 * hold an unbounded collection — it has to hold <em>components</em>, and a binary operator
 * holding two is as much a composite as a directory holding a hundred.
 */
public abstract class BinaryOperation implements Expression {

    protected final Expression left;
    protected final Expression right;

    protected BinaryOperation(Expression left, Expression right) {
        this.left = Objects.requireNonNull(left);
        this.right = Objects.requireNonNull(right);
    }

    protected abstract String symbol();

    @Override
    public final String toText() {
        return "(" + left.toText() + " " + symbol() + " " + right.toText() + ")";
    }

    @Override
    public final int nodeCount() {
        return 1 + left.nodeCount() + right.nodeCount();
    }
}
