package dev.kaldiroglu.dp.behavioral.visitor.checkout.problem;

import java.util.List;

/**
 * Stage two: one tax class with an overload for each kind. First each item is held as its own
 * type, so its own {@code taxOf} is called and the tax is correct. Then the same items are held
 * as {@code Item}s, as the cart holds them, so {@code taxOf(Item)} is called for every item and
 * every item is taxed at the standard rate.
 */
public final class StageTwoMain {

    public static void main(String[] args) {
        OverloadedTax tax = new OverloadedTax();
        Book book = new Book("Novel", 40);
        Food food = new Food("Coffee", 100);
        Electronics electronics = new Electronics("Headphones", 500);

        System.out.println("Each item held as its own type, so taxOf(Book), taxOf(Food) and taxOf(Electronics) are called:");
        print(book, tax.taxOf(book));
        print(food, tax.taxOf(food));
        print(electronics, tax.taxOf(electronics));
        System.out.println("  total tax " + (tax.taxOf(book) + tax.taxOf(food) + tax.taxOf(electronics)));

        List<Item> cart = List.of(book, food, electronics);
        System.out.println("Each item held as an Item, as in the cart, so taxOf(Item) is called:");
        for (Item item : cart) {
            print(item, tax.taxOf(item));
        }
        System.out.println("  total tax " + tax.total(cart));
    }

    private static void print(Item item, int tax) {
        System.out.printf("  %-10s %3d  tax %3d%n", item.name(), item.price(), tax);
    }
}
