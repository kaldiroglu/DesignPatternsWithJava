package dev.kaldiroglu.dp.structural.decorator.toast.solution;

/** A decorator that multiplies rather than adds, and where it sits changes the bill. */
public final class PromotionPositionMain {

    private PromotionPositionMain() {
    }

    public static void main(String[] args) {
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
}
