package dev.kaldiroglu.dp.behavioral.state.order.solution;

/** A <b>ConcreteState</b>: the end. Every operation is refused. */
public record Delivered() implements OrderState {

    @Override
    public String name() {
        return "delivered";
    }
}
