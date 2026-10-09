package dev.kaldiroglu.dp.behavioral.visitor.checkout.modern;

/**
 * The same items as a <b>sealed</b> interface (Java 17) with record implementations.
 * <p>
 * A {@code switch} over a sealed type must cover every permitted class, or it does not
 * compile (Java 21). That gives the same check as the visitor's interface — a new item kind
 * is a compile error in every switch that forgets it — without {@code accept} and
 * {@code visit}.
 */
public sealed interface Item permits Book, Food, Electronics, GiftCard {

    String name();

    int price();
}
