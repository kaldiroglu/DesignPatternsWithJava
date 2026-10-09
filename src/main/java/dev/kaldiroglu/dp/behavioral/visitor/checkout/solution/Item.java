package dev.kaldiroglu.dp.behavioral.visitor.checkout.solution;

/**
 * The <b>Element</b>: an item accepts a visitor and calls it back.
 * <p>
 * That call back is the point. Inside {@code Book.accept}, {@code this} has the static type
 * {@code Book}, so {@code visitor.visit(this)} chooses {@code visit(Book)} at compile time.
 * The first call chooses by the item's run-time type, the second by the visitor's: GoF call
 * this double dispatch.
 */
public interface Item {

    String name();

    int price();

    <R> R accept(ItemVisitor<R> visitor);
}
