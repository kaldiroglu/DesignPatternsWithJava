package dev.kaldiroglu.dp.behavioral.state.order.solution;

/** A <b>ConcreteState</b>: the order exists and is not paid. It may be paid or cancelled. */
public record Placed() implements OrderState {

    @Override
    public String name() {
        return "placed";
    }

    @Override
    public OrderState pay(Order order) {
        order.event("paid");
        return new Paid();
    }

    @Override
    public OrderState cancel(Order order) {
        order.event("cancelled");
        return new Cancelled();
    }
}
