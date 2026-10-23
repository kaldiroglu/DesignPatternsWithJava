package dev.kaldiroglu.dp.behavioral.visitor.checkout.problem;

import java.util.List;

/**
 * Stage two: one tax class with an overload for each kind. Each item is held as an
 * {@code Item}, so {@code taxOf(Item)} is called and every item is taxed at the standard rate.
 */
public final class StageTwoMain {

    public static void main(String[] args) {
        List<Item> cart = SampleCart.threeItems();
        System.out.println("A book for 40, food for 100, electronics for 500: the correct tax is 2 + 1 + 100 = "
                + (40 * Rates.BOOK_TAX / 100 + 100 * Rates.FOOD_TAX / 100 + 500 * Rates.ELECTRONICS_TAX / 100));
        System.out.println("Stage two, overloads:   tax " + new OverloadedTax().total(cart)
                + " (each item is held as an Item, so taxOf(Item) is called)");
    }
}
