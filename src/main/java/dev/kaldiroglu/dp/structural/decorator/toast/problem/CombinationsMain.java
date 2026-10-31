package dev.kaldiroglu.dp.structural.decorator.toast.problem;

import java.util.List;

/** The counting argument: how many classes a class per combination needs. */
public class CombinationsMain {

    /** Cheese, sausage, tomato, ketchup, salad: the five the solution package offers. */
    private static final int TOPPING_COUNT = 5;

    public static void main(String[] args) {
        List<Toast> menu = Menu.items();

        // A class per combination of n toppings is 2^n - 1 classes, because each topping is
        // independently on or off and the empty toast is the bread.
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
        boolean ok = menu.get(0).calculatePrice() == 8      // cheese
                && menu.get(1).calculatePrice() == 10       // cheese + tomato
                && menu.get(2).calculatePrice() == 8        // sausage
                && menu.get(3).calculatePrice() == 10       // sausage + tomato
                && menu.get(4).calculatePrice() == 11       // cheese + sausage, hand-copied
                && combinations == 31;
        System.out.println(ok ? "self-check: prices as expected" : "self-check: FAILED");
    }
}
