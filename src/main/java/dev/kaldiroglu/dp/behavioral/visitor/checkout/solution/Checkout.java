package dev.kaldiroglu.dp.behavioral.visitor.checkout.solution;

import java.util.List;

/** The <b>ObjectStructure</b>: the cart. It walks its items and sends each one the visitor. */
public final class Checkout {

    private final List<Item> cart;

    public Checkout(List<Item> cart) {
        this.cart = List.copyOf(cart);
    }

    public int total(ItemVisitor<Integer> visitor) {
        int total = 0;
        for (Item item : cart) {
            total += item.accept(visitor);
        }
        return total;
    }

    public List<String> lines(ItemVisitor<String> visitor) {
        return cart.stream().map(item -> item.accept(visitor)).toList();
    }
}
