package dev.kaldiroglu.dp.structural.decorator.toast.solution;

/** Double cheese: one line here, and no class anywhere in the problem package. */
public final class DoubleCheeseMain {

    private DoubleCheeseMain() {
    }

    public static void main(String[] args) {
        Toastable toast = new Cheese(new Cheese(new ToastBread()));

        System.out.println("\nDouble cheese: " + toast.calculatePrice()
                + "  (bread 5 + cheese 3 + cheese 3)");
        System.out.println("  A decorator wraps a Toastable, and a decorated toast is one too,");
        System.out.println("  so nothing stops the same topping being applied again.");
    }
}
