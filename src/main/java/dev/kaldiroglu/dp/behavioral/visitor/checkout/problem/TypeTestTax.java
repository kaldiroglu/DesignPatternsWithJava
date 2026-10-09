package dev.kaldiroglu.dp.behavioral.visitor.checkout.problem;

import java.util.List;

/**
 * Stage three: one class for the tax, which tests the type of each item.
 * <p>
 * Correct for every item the shop sold when it was written. The last branch charges the
 * standard rate, so an item kind added later is taxed without any error — a gift card is
 * charged 20 percent, and nothing tells the developer that a branch is missing.
 */
public final class TypeTestTax {

    public int taxOf(Item item) {
        if (item instanceof Book book) {
            return book.price() * Rates.BOOK_TAX / 100;
        } else if (item instanceof Food food) {
            return food.price() * Rates.FOOD_TAX / 100;
        } else if (item instanceof Electronics electronics) {
            return electronics.price() * Rates.ELECTRONICS_TAX / 100;
        } else {
            return item.price() * Rates.STANDARD_TAX / 100;
        }
    }

    public int total(List<Item> cart) {
        return cart.stream().mapToInt(this::taxOf).sum();
    }
}
