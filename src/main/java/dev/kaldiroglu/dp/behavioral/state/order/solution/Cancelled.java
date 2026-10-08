package dev.kaldiroglu.dp.behavioral.state.order.solution;

/** A <b>ConcreteState</b>: the other end. Every operation is refused. */
public record Cancelled() implements OrderState {

    @Override
    public String name() {
        return "cancelled";
    }
}
