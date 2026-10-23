package dev.kaldiroglu.dp.behavioral.visitor.checkout.problem;

import java.util.List;

/**
 * Stage three after a gift card is added. The gift card falls into the else branch, so it
 * is taxed and shipped at the standard rates.
 */
public final class GiftCardMain {

    public static void main(String[] args) {
        List<Item> cart = SampleCart.threeItems();
        cart.add(new GiftCard("Gift card", 100));
        System.out.println("A gift card for 100 is added. It has no tax and is not shipped.");
        System.out.println("Stage three, type tests: tax " + new TypeTestTax().total(cart)
                + ", shipping " + new TypeTestShipping().total(cart)
                + " (the else branch charges the standard rates)");
    }
}
