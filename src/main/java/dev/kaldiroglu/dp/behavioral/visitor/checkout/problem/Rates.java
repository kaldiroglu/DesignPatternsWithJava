package dev.kaldiroglu.dp.behavioral.visitor.checkout.problem;

/** The shop's own tax rates, in percent, and shipping costs. */
public final class Rates {

    public static final int BOOK_TAX = 5;
    public static final int FOOD_TAX = 1;
    public static final int ELECTRONICS_TAX = 20;
    public static final int STANDARD_TAX = 20;

    public static final int BOOK_SHIPPING = 10;
    public static final int FOOD_SHIPPING = 15;
    public static final int ELECTRONICS_SHIPPING = 25;
    public static final int STANDARD_SHIPPING = 10;

    private Rates() {
    }
}
