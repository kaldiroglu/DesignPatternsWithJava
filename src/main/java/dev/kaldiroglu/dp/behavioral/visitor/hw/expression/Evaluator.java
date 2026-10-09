package dev.kaldiroglu.dp.behavioral.visitor.hw.expression;

/** Computes the value. The visitor walks the tree by calling {@code accept} on the children. */
public final class Evaluator implements ExprVisitor<Integer> {

    @Override
    public Integer visit(Num num) {
        return num.value();
    }

    @Override
    public Integer visit(Add add) {
        return add.left().accept(this) + add.right().accept(this);
    }

    @Override
    public Integer visit(Mul mul) {
        return mul.left().accept(this) * mul.right().accept(this);
    }

    @Override
    public Integer visit(Neg neg) {
        return -neg.operand().accept(this);
    }
}
