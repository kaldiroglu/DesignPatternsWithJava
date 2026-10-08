package dev.kaldiroglu.dp.behavioral.state.order.solution;

import dev.kaldiroglu.dp.behavioral.state.order.problem.EnumOrder;

/**
 * Ships an order, fails three deliveries, ships it again and fails once more — first with
 * stage three's enum, then with State objects.
 */
public final class Main {

    public static void main(String[] args) {
        EnumOrder before = new EnumOrder();
        before.pay();
        before.ship("TR-1");
        before.failDelivery();
        before.failDelivery();
        before.failDelivery();
        before.ship("TR-2");
        before.failDelivery();
        System.out.println("Enum constants: " + before.events());
        System.out.println("  status now: " + before.status());

        Order after = new Order();
        after.pay();
        after.ship("TR-1");
        after.failDelivery();
        after.failDelivery();
        after.failDelivery();
        after.ship("TR-2");
        after.failDelivery();
        System.out.println("State objects:  " + after.events());
        System.out.println("  state now: " + after.state());

        try {
            after.cancel();
        } catch (IllegalStateException refused) {
            System.out.println("Cancel a shipped order: " + refused.getMessage());
        }
    }
}
