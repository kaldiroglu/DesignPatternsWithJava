package dev.kaldiroglu.dp.structural.decorator.toast.solution;

/** The finished toast written the way the chain actually nests. */
public final class OneExpressionMain {

    private OneExpressionMain() {
    }

    public static void main(String[] args) {
        Toastable toast =
                new Salad(
                        new Ketchup(
                                new Tomato(
                                        new Sausage(
                                                new Cheese(
                                                        new ToastBread())))));

        System.out.println("\nThe whole Ayvalik toast at once: " + toast.calculatePrice());
        toast.getToppings().forEach(topping -> System.out.println("    " + topping));
    }
}
