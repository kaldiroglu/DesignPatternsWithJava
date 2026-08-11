package dev.kaldiroglu.dp.structural.decorator.toast.solution;

/**
 * The same shop, built out of one class per topping instead of one class per combination.
 * <p>
 * Five topping classes replace the thirty-one the problem package would have needed, and
 * the last two demonstrations are things the problem package cannot express at all.
 * <p>
 * Each demonstration also has its own {@code Main} in this package, so a single one can be
 * run without editing this file. This class runs them in order and then checks every
 * number they printed.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        StepByStepMain.main(args);
        OneExpressionMain.main(args);
        DoubleCheeseMain.main(args);
        PromotionPositionMain.main(args);
        selfCheck();
    }

    /** Every number this demo prints, checked against what it should be. */
    private static void selfCheck() {
        Toastable full = new Salad(new Ketchup(new Tomato(new Sausage(new Cheese(new ToastBread())))));

        boolean ok = new ToastBread().calculatePrice() == 5
                && full.calculatePrice() == 16
                && full.getToppings().size() == 5
                && new Cheese(new Cheese(new ToastBread())).calculatePrice() == 11
                && new Promotion(full, "student discount", 25).calculatePrice() == 12
                && new Salad(new Ketchup(new Tomato(new Sausage(new Cheese(
                        new Promotion(new ToastBread(), "student discount", 25)))))).calculatePrice() == 14;

        System.out.println("\nself-check: " + (ok ? "all prices as expected" : "FAILED"));
    }
}
