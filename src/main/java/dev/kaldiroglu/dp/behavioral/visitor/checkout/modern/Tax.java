package dev.kaldiroglu.dp.behavioral.visitor.checkout.modern;

import java.util.List;

/**
 * The tax rule as one switch. There is no {@code default}: if a fifth item kind is permitted,
 * this switch stops compiling until it has a case for it.
 */
public final class Tax {

    private Tax() {
    }

    public static int of(Item item) {
        return switch (item) {
            case Book book -> book.price() * 5 / 100;
            case Food food -> food.price() * 1 / 100;
            case Electronics electronics -> electronics.price() * 20 / 100;
            case GiftCard giftCard -> 0;
        };
    }

    public static int total(List<Item> cart) {
        return cart.stream().mapToInt(Tax::of).sum();
    }
}
