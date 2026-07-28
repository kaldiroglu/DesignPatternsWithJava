package dev.kaldiroglu.dp.structural.composite.hw.expression;

/**
 * A ConcreteComposite that can fail.
 * <p>
 * Worth having: it forces the question of what a Composite operation does when one node
 * cannot answer. Throwing is the honest choice here — a wrong number is worse than no number.
 */
public final class Divide extends BinaryOperation {

    public Divide(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected String symbol() {
        return "/";
    }

    @Override
    public double evaluate() {
        double divisor = right.evaluate();
        if (divisor == 0) {
            throw new ArithmeticException("division by zero in " + toText());
        }
        return left.evaluate() / divisor;
    }
}
