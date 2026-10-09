package dev.kaldiroglu.dp.behavioral.visitor.checkout.problem;

import java.util.ArrayList;
import java.util.List;

/**
 * Stages two and three on one cart. The overloads tax every item at the standard rate;
 * the type tests are right until a gift card is added, which falls into the else branch.
 */
public final class Main {

    public static void main(String[] args) {
        List<Item> cart = new ArrayList<>(List.of(new Book("Novel", 40), new Food("Coffee", 100),
                new Electronics("Headphones", 500)));
        System.out.println("A book for 40, food for 100, electronics for 500: the correct tax is 2 + 1 + 100 = "
                + (40 * Rates.BOOK_TAX / 100 + 100 * Rates.FOOD_TAX / 100 + 500 * Rates.ELECTRONICS_TAX / 100));
        System.out.println("Stage two, overloads:   tax " + new OverloadedTax().total(cart)
                + " (each item is held as an Item, so taxOf(Item) is called)");
        System.out.println("Stage three, type tests: tax " + new TypeTestTax().total(cart)
                + ", shipping " + new TypeTestShipping().total(cart));

        cart.add(new GiftCard("Gift card", 100));
        System.out.println("A gift card for 100 is added. It has no tax and is not shipped.");
        System.out.println("Stage three, type tests: tax " + new TypeTestTax().total(cart)
                + ", shipping " + new TypeTestShipping().total(cart)
                + " (the else branch charges the standard rates)");
    }
}
