package dev.kaldiroglu.dp.behavioral.state.order.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * The <b>Context</b>: an order that forwards every request to its current state.
 * <p>
 * Compare the three orders in {@code problem}. This one has no flags, no switch and no
 * fields that belong to one status. Each method is one line: ask the state, and keep the
 * state it answers with. To the caller, the order seems to change its class as its status
 * changes — which is how GoF's intent puts it.
 */
public final class Order {

    private OrderState state = new Placed();
    private final List<String> events = new ArrayList<>();

    public void pay() {
        state = state.pay(this);
    }

    public void ship(String trackingNumber) {
        state = state.ship(this, trackingNumber);
    }

    public void failDelivery() {
        state = state.failDelivery(this);
    }

    public void deliver() {
        state = state.deliver(this);
    }

    public void cancel() {
        state = state.cancel(this);
    }

    public OrderState state() {
        return state;
    }

    void event(String event) {
        events.add(event);
    }

    public List<String> events() {
        return List.copyOf(events);
    }
}
