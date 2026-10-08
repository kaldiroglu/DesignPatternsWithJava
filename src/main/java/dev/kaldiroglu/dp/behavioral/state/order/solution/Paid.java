package dev.kaldiroglu.dp.behavioral.state.order.solution;

/** A <b>ConcreteState</b>: paid and waiting in the warehouse. It may be shipped or cancelled. */
public record Paid() implements OrderState {

    @Override
    public String name() {
        return "paid";
    }

    @Override
    public OrderState ship(Order order, String trackingNumber) {
        order.event("shipped " + trackingNumber);
        return new Shipped(trackingNumber, 0);
    }

    @Override
    public OrderState cancel(Order order) {
        order.event("cancelled, refund issued");
        return new Cancelled();
    }
}
