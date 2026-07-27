package dev.kaldiroglu.dp.structural.decorator.toast.solution;

/**
 * The same shop, built out of one class per topping instead of one class per combination.
 * <p>
 * Five topping classes replace the thirty-one the problem package would have needed, and the
 * last three demonstrations are things the problem package cannot express at all.
 */
public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        main.buildAyvalikToastStepByStep();
        main.buildAyvalikToastInOneExpression();
        main.theSameToppingTwice();
        main.aDecoratorThatIsNotATopping();
        main.selfCheck();
    }

    /** Adds one topping at a time, so the price can be watched climbing. */
    public void buildAyvalikToastStepByStep() {
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
        printToppings(toast);
    }

    /** The finished toast written the way the chain actually nests. */
    public void buildAyvalikToastInOneExpression() {
        Toastable toast =
                new Salad(
                        new Ketchup(
                                new Tomato(
                                        new Sausage(
                                                new Cheese(
                                                        new ToastBread())))));

        System.out.println("\nThe whole Ayvalik toast at once: " + toast.calculatePrice());
        printToppings(toast);
    }

    /** Double cheese: one line here, and no class anywhere in the problem package. */
    public void theSameToppingTwice() {
        Toastable toast = new Cheese(new Cheese(new ToastBread()));

        System.out.println("\nDouble cheese: " + toast.calculatePrice()
                + "  (bread 5 + cheese 3 + cheese 3)");
        System.out.println("  A decorator wraps a Toastable, and a decorated toast is one too,");
        System.out.println("  so nothing stops the same topping being applied again.");
    }

    /** A decorator that multiplies rather than adds, and where it sits changes the bill. */
    public void aDecoratorThatIsNotATopping() {
        Toastable full = new Salad(new Ketchup(new Tomato(new Sausage(new Cheese(new ToastBread())))));

        Toastable discountOutside = new Promotion(full, "student discount", 25);
        Toastable discountInside = new Salad(new Ketchup(new Tomato(new Sausage(
                new Cheese(new Promotion(new ToastBread(), "student discount", 25))))));

        System.out.println("\nA 25% discount, same toast, two positions in the chain:");
        System.out.println("  outermost, so it discounts everything   " + discountOutside.calculatePrice());
        System.out.println("  innermost, so it discounts only bread   " + discountInside.calculatePrice());
        System.out.println("  Order is a design decision, not a detail. The pattern makes it");
        System.out.println("  visible and cheap to change; the problem package made it neither.");
    }

    private void printToppings(Toastable toast) {
        toast.getToppings().forEach(topping -> System.out.println("    " + topping));
    }

    /** Every number this demo prints, checked against what it should be. */
    private void selfCheck() {
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
