package dev.kaldiroglu.dp.behavioral.state.order.solution;

/**
 * A shipped order is asked to cancel. The Shipped state does not allow it, so the default in
 * OrderState refuses.
 */
public final class RefusedRequestMain {

    public static void main(String[] args) {
        Order order = new Order();
        order.pay();
        order.ship("TR-1");
        try {
            order.cancel();
        } catch (IllegalStateException refused) {
            System.out.println("Cancel a shipped order: " + refused.getMessage());
        }
    }
}
