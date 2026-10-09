package dev.kaldiroglu.dp.behavioral.visitor.checkout.solution;

/**
 * The <b>Visitor</b>: one method for each kind of item.
 * <p>
 * When the catalog team adds {@link GiftCard}, they add {@code visit(GiftCard)} here, and every
 * visitor that does not handle gift cards stops compiling. A missing case is a compile error,
 * not a wrong total.
 *
 * @param <R> what the operation returns for one item
 */
public interface ItemVisitor<R> {

    R visit(Book book);

    R visit(Food food);

    R visit(Electronics electronics);

    R visit(GiftCard giftCard);
}
