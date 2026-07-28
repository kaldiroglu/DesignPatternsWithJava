package dev.kaldiroglu.dp.structural.composite.hw.expression;

/** A ConcreteComposite. */
public final class Add extends BinaryOperation {

    public Add(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected String symbol() {
        return "+";
    }

    @Override
    public double evaluate() {
        return left.evaluate() + right.evaluate();
    }
}
