package dev.kaldiroglu.dp.structural.decorator.toast.problem;

import java.util.List;

/**
 * Runs the menu the shop can actually sell, and then shows what it cannot sell.
 * <p>
 * The output is the argument. Six classes buy five toasts, the sixth of which had to be
 * written by copying numbers out of two other classes, and the customer's next request needs
 * a seventh class, a recompile and a redeploy.
 */
public class Main {

    /** Cheese, sausage, tomato, ketchup, salad: the five the solution package offers. */
    private static final int TOPPING_COUNT = 5;

    public static void main(String[] args) {
        List<Toast> menu = List.of(
                new CheeseToast(),
                new CheeseTomatoToast(),
                new SausageToast(),
                new SausageTomatoToast(),
                new CheeseSausageToast());

        System.out.println("The menu, one class per item:");
        menu.forEach(toast -> System.out.println("  " + toast));

        System.out.println("""

                What the shop cannot sell:
                  cheese + ketchup            no class
                  sausage + tomato + salad    no class
                  tomato added to a sausage toast, at the counter, after ordering
                                              no class, and no way to write one

                Adding a topping is a code change. Adding it to an order already
                placed is impossible: a toast's toppings are fixed by its type, and
                a Java object cannot change its type after it is constructed.""");

        // The counting argument. A class per combination of n toppings is 2^n - 1 classes,
        // because each topping is independently on or off and the empty toast is the bread.
        int combinations = (1 << TOPPING_COUNT) - 1;
        System.out.printf("""

                        With %d toppings, a class per combination is 2^%d - 1 = %d classes.
                        This package has %d, and one of those five items already needed
                        hand-copied prices because Java has no multiple inheritance.
                        %n""",
                TOPPING_COUNT, TOPPING_COUNT, combinations, menu.size());

        selfCheck(menu, combinations);
    }

    /** Every number this demo puts on screen, checked against what it should be. */
    private static void selfCheck(List<Toast> menu, int combinations) {
        boolean ok = menu.get(0).calculatePrice() == 5      // cheese
                && menu.get(1).calculatePrice() == 7        // cheese + tomato
                && menu.get(2).calculatePrice() == 6        // sausage
                && menu.get(3).calculatePrice() == 8        // sausage + tomato
                && menu.get(4).calculatePrice() == 8        // cheese + sausage, hand-copied
                && combinations == 31;
        System.out.println(ok ? "self-check: prices as expected" : "self-check: FAILED");
    }
}
