package dev.kaldiroglu.dp.behavioral.visitor.checkout.solution;

/** A <b>ConcreteElement</b>: data, and one line that calls the visitor back. */
public record GiftCard(String name, int price) implements Item {

    @Override
    public <R> R accept(ItemVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
