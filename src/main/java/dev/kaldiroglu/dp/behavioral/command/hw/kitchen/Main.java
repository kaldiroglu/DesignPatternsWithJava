package dev.kaldiroglu.dp.behavioral.command.hw.kitchen;

/**
 * Shows orders waiting on a rail: one can be cancelled while it waits, and not after it is
 * cooked.
 */
public class Main {

    public static void main(String[] args) {
        Kitchen kitchen = new Kitchen();
        OrderRail rail = new OrderRail();
        Order soup = new Order(kitchen, "soup", 4);
        Order salad = new Order(kitchen, "salad", 2);
        Order kebab = new Order(kitchen, "kebab", 4);

        rail.place(soup);
        rail.place(salad);
        rail.place(kebab);
        System.out.println("Orders waiting on the rail: " + rail.waiting());

        rail.cookNext();
        System.out.println("The kitchen cooked: " + kitchen.cooked());

        System.out.println("Cancel the salad, still waiting: " + rail.cancel(salad));
        System.out.println("Cancel the soup, already cooked: " + rail.cancel(soup));

        rail.cookNext();
        System.out.println("The kitchen cooked: " + kitchen.cooked());
        System.out.println("Orders waiting on the rail: " + rail.waiting());
    }
}
