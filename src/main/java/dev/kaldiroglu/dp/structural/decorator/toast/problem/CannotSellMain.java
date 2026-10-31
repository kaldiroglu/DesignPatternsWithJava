package dev.kaldiroglu.dp.structural.decorator.toast.problem;

/** What the menu cannot sell, because a toast's toppings are fixed by its type. */
public class CannotSellMain {

    public static void main(String[] args) {
        System.out.println("""
                What the shop cannot sell:
                  cheese + ketchup            no class
                  sausage + tomato + salad    no class
                  tomato added to a sausage toast, at the counter, after ordering
                                              no class, and no way to write one

                Adding a topping is a code change. Adding it to an order already
                placed is impossible: a toast's toppings are fixed by its type, and
                a Java object cannot change its type after it is constructed.""");
    }
}
