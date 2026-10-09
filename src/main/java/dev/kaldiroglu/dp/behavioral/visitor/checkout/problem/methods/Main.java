package dev.kaldiroglu.dp.behavioral.visitor.checkout.problem.methods;

import java.util.List;

/**
 * Stage one: each item computes its own tax, shipping cost and receipt line. The figures
 * are right; the cost is that every new operation is an edit to every item class.
 */
public final class Main {

    public static void main(String[] args) {
        List<Item> cart = List.of(new Book("Novel", 40), new Food("Coffee", 100),
                new Electronics("Headphones", 500));
        int tax = 0;
        int shipping = 0;
        for (Item item : cart) {
            System.out.println(item.receiptLine());
            tax += item.tax();
            shipping += item.shippingCost();
        }
        System.out.println("Tax " + tax + ", shipping " + shipping);
        System.out.println("Three operations, written in each of the three item classes.");
    }
}
