package dev.kaldiroglu.dp.behavioral.visitor.checkout.problem;

import java.util.List;

/**
 * Stage three: the tax class tests the type of each item. With three items the tax and the
 * shipping are correct.
 */
public final class StageThreeMain {

    public static void main(String[] args) {
        List<Item> cart = SampleCart.threeItems();
        System.out.println("Stage three, type tests: tax " + new TypeTestTax().total(cart)
                + ", shipping " + new TypeTestShipping().total(cart));
    }
}
