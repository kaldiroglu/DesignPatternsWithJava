package dev.kaldiroglu.dp.behavioral.visitor.checkout.problem;

import java.util.List;

/**
 * Stage two: one class for the tax, with one method per kind of item.
 * <p>
 * The items stay clean, and the tax rules are in one place. But {@link #total} holds each
 * item as an {@code Item}. Java chooses between overloads at compile time, from the static
 * type of the argument, so {@code taxOf(item)} always calls {@code taxOf(Item)}. Without that
 * method the loop does not compile; with it, every item is taxed at the standard rate.
 */
public final class OverloadedTax {

    public int taxOf(Book book) {
        return book.price() * Rates.BOOK_TAX / 100;
    }

    public int taxOf(Food food) {
        return food.price() * Rates.FOOD_TAX / 100;
    }

    public int taxOf(Electronics electronics) {
        return electronics.price() * Rates.ELECTRONICS_TAX / 100;
    }

    public int taxOf(Item item) {
        return item.price() * Rates.STANDARD_TAX / 100;
    }

    public int total(List<Item> cart) {
        int total = 0;
        for (Item item : cart) {
            total += taxOf(item);       // always taxOf(Item)
        }
        return total;
    }
}
