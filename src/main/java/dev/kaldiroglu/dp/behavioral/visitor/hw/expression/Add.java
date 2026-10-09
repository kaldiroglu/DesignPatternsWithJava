package dev.kaldiroglu.dp.behavioral.visitor.hw.expression;

public record Add(Expr left, Expr right) implements Expr {

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
