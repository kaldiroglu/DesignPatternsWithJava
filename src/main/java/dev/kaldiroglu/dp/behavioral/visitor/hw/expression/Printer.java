package dev.kaldiroglu.dp.behavioral.visitor.hw.expression;

/** Writes the expression with brackets around every sum and product. */
public final class Printer implements ExprVisitor<String> {

    @Override
    public String visit(Num num) {
        return String.valueOf(num.value());
    }

    @Override
    public String visit(Add add) {
        return "(" + add.left().accept(this) + " + " + add.right().accept(this) + ")";
    }

    @Override
    public String visit(Mul mul) {
        return "(" + mul.left().accept(this) + " * " + mul.right().accept(this) + ")";
    }

    @Override
    public String visit(Neg neg) {
        return "-" + neg.operand().accept(this);
    }
}
