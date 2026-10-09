package dev.kaldiroglu.dp.behavioral.visitor.hw.expression;

/**
 * The <b>Visitor</b>, generic in what it returns.
 * <p>
 * Homework 3 added {@link Neg}. That meant one new record and one new method here — and
 * then {@link Evaluator} and {@link Printer} did not compile until each had
 * {@code visit(Neg)}. A new operation, {@link DepthCounter}, was one new class and no other
 * change. That is GoF's trade-off: new operations are easy, new element classes are hard.
 */
public interface ExprVisitor<R> {

    R visit(Num num);

    R visit(Add add);

    R visit(Mul mul);

    R visit(Neg neg);
}
