package dev.kaldiroglu.dp.behavioral.visitor.checkout.problem;

/**
 * An item the shop sells, as the catalog team writes it: data only.
 * <p>
 * The interface is not sealed. The catalog team adds new kinds of item when the shop starts
 * selling them, and checkout code is not told.
 */
public interface Item {

    String name();

    int price();
}
