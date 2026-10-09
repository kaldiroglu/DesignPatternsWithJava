package dev.kaldiroglu.dp.behavioral.visitor.interpreter;

/**
 * Interpreter: a small rule language for a shop, such as
 * "category is books and price below 50".
 * <p>
 * Each grammar rule is one class, and a sentence is a tree of them: a Composite. Every class
 * has {@link #interpret}, which evaluates its part of the sentence against a product — the
 * context. GoF's own sample code interprets Boolean expressions in the same way.
 * <p>
 * The operations are inside the classes. That suits a small language with few operations.
 * When the operations grow — printing, checking, optimizing — GoF suggest moving them into
 * visitors, so the rule classes stay small.
 */
public sealed interface Rule permits CategoryIs, PriceBelow, And, Or, Not {

    boolean interpret(Product product);

    String describe();
}
