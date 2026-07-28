package dev.kaldiroglu.dp.structural.composite.hw.expression;

/** A Leaf: a literal value. It evaluates to itself and has nothing below it. */
public final class Number implements Expression {

    private final double value;

    public Number(double value) {
        this.value = value;
    }

    @Override
    public double evaluate() {
        return value;
    }

    @Override
    public String toText() {
        return value == Math.rint(value) ? String.valueOf((long) value) : String.valueOf(value);
    }

    @Override
    public int nodeCount() {
        return 1;
    }
}
