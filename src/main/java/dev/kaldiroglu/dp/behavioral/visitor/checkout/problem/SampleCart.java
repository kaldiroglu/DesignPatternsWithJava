package dev.kaldiroglu.dp.behavioral.visitor.checkout.problem;

import java.util.ArrayList;
import java.util.List;

/**
 * The cart that stages two and three run on: a book for 40, food for 100 and electronics
 * for 500. Each call returns a new list, so a caller can add a gift card to it.
 */
final class SampleCart {

    private SampleCart() {
    }

    static List<Item> threeItems() {
        return new ArrayList<>(List.of(new Book("Novel", 40), new Food("Coffee", 100),
                new Electronics("Headphones", 500)));
    }
}
