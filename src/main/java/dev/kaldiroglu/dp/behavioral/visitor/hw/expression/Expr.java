package dev.kaldiroglu.dp.behavioral.visitor.hw.expression;

/** Homework 3: an arithmetic expression as a tree. */
public interface Expr {

    <R> R accept(ExprVisitor<R> visitor);
}
