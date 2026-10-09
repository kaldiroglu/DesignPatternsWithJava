package dev.kaldiroglu.dp.behavioral.visitor.checkout.modern;

import java.util.List;

/**
 * The tax of the cart with a sealed interface and a switch. The gift card has its own
 * case, so it is taxed 0; a fifth item kind would not compile until the switch has a case.
 */
public final class Main {

    public static void main(String[] args) {
        List<Item> cart = List.of(new Book("Novel", 40), new Food("Coffee", 100),
                new Electronics("Headphones", 500), new GiftCard("Gift card", 100));
        for (Item item : cart) {
            System.out.println(item.name() + " " + item.price() + ": tax " + Tax.of(item));
        }
        System.out.println("Total tax: " + Tax.total(cart));
    }
}
