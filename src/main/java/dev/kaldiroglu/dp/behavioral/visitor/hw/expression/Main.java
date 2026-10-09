package dev.kaldiroglu.dp.behavioral.visitor.hw.expression;

/** Runs three visitors over one expression: it is printed, evaluated and measured. */
public final class Main {

    public static void main(String[] args) {
        Expr expression = new Mul(new Add(new Num(2), new Num(3)), new Neg(new Num(4)));
        System.out.println("Expression: " + expression.accept(new Printer()));
        System.out.println("Value:      " + expression.accept(new Evaluator()));
        System.out.println("Depth:      " + expression.accept(new DepthCounter()));
    }
}
