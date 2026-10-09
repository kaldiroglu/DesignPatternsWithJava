package dev.kaldiroglu.dp.behavioral.visitor.hw.expression;

public record Num(int value) implements Expr {

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
