package dev.kaldiroglu.dp.behavioral.visitor.checkout.problem;

import java.util.List;

/** Stage three again, for shipping: the same chain of type tests, with the same last branch. */
public final class TypeTestShipping {

    public int costOf(Item item) {
        if (item instanceof Book) {
            return Rates.BOOK_SHIPPING;
        } else if (item instanceof Food) {
            return Rates.FOOD_SHIPPING;
        } else if (item instanceof Electronics) {
            return Rates.ELECTRONICS_SHIPPING;
        } else {
            return Rates.STANDARD_SHIPPING;
        }
    }

    public int total(List<Item> cart) {
        return cart.stream().mapToInt(this::costOf).sum();
    }
}
