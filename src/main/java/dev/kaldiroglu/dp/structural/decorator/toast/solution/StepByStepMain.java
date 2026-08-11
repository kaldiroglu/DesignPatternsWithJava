package dev.kaldiroglu.dp.structural.decorator.toast.solution;

/** Adds one topping at a time, so the price can be watched climbing. */
public final class StepByStepMain {

    private StepByStepMain() {
    }

    public static void main(String[] args) {
        System.out.println("Building an Ayvalik toast, one topping at a time:");

        Toastable toast = new ToastBread();
        System.out.println("  bare bread          " + toast.calculatePrice());

        toast = new Cheese(toast);
        System.out.println("  + cheddar cheese    " + toast.calculatePrice());

        toast = new Sausage(toast);
        System.out.println("  + sucuk sausage     " + toast.calculatePrice());

        toast = new Tomato(toast);
        System.out.println("  + tomato            " + toast.calculatePrice());

        toast = new Ketchup(toast);
        System.out.println("  + ketchup           " + toast.calculatePrice());

        toast = new Salad(toast);
        System.out.println("  + Russian salad     " + toast.calculatePrice());

        // Every line above assigned to the same variable of the same type. The toast grew
        // five new responsibilities and never changed its type, which is what the problem
        // package could not do at any price.
        toast.getToppings().forEach(topping -> System.out.println("    " + topping));
    }
}
