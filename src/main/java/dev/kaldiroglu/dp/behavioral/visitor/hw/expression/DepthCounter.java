package dev.kaldiroglu.dp.behavioral.visitor.hw.expression;

/** A new operation: the depth of the tree. One new class; nothing else changed. */
public final class DepthCounter implements ExprVisitor<Integer> {

    @Override
    public Integer visit(Num num) {
        return 1;
    }

    @Override
    public Integer visit(Add add) {
        return 1 + Math.max(add.left().accept(this), add.right().accept(this));
    }

    @Override
    public Integer visit(Mul mul) {
        return 1 + Math.max(mul.left().accept(this), mul.right().accept(this));
    }

    @Override
    public Integer visit(Neg neg) {
        return 1 + neg.operand().accept(this);
    }
}
