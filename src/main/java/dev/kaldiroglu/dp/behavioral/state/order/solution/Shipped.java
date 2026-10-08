package dev.kaldiroglu.dp.behavioral.state.order.solution;

/**
 * A <b>ConcreteState</b> with its own data: the tracking number and the failed delivery
 * attempts of this one shipment.
 * <p>
 * Compare {@code problem.OrderStatus.SHIPPED}, whose data had to live in the order. Here a
 * new shipment is a new {@code Shipped} object, so its count starts at zero — nothing has to
 * remember to reset it. When the shipment ends, its data ends with it.
 */
public record Shipped(String trackingNumber, int failedAttempts) implements OrderState {

    public static final int MAX_ATTEMPTS = 3;

    @Override
    public String name() {
        return "shipped";
    }

    @Override
    public OrderState failDelivery(Order order) {
        int attempts = failedAttempts + 1;
        order.event("delivery attempt " + attempts + " failed");
        if (attempts == MAX_ATTEMPTS) {
            order.event("back to the warehouse");
            return new Paid();
        }
        return new Shipped(trackingNumber, attempts);
    }

    @Override
    public OrderState deliver(Order order) {
        order.event("delivered");
        return new Delivered();
    }
}
