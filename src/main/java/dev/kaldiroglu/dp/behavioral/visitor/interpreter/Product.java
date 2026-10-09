package dev.kaldiroglu.dp.behavioral.visitor.interpreter;

/** The context the rules are interpreted against: one product in a shop. */
public record Product(String name, String category, int price) {
}
