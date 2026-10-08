package dev.kaldiroglu.dp.behavioral.state.order.problem;

import java.util.ArrayList;
import java.util.List;

/**
 * Stage one: <b>the order's status is four boolean flags.</b>
 * <p>
 * Every method checks the flags it needs. It works, and every rule of the order can be
 * found in these four methods. What it costs:
 * <ul>
 *   <li>Four flags make sixteen combinations, and only five of them are real statuses. Nothing
 *       stops a bug from making an order that is both cancelled and shipped.</li>
 *   <li>Each method needs its own chain of {@code if}s, and each chain must list every flag
 *       that matters to it.</li>
 * </ul>
 */
public final class FlagOrder {

    private boolean paid;
    private boolean shipped;
    private boolean delivered;
    private boolean cancelled;
    private final List<String> events = new ArrayList<>();

    public void pay() {
        if (cancelled || paid) {
            throw new IllegalStateException("cannot pay this order");
        }
        paid = true;
        events.add("paid");
    }

    public void ship(String trackingNumber) {
        if (!paid || shipped || cancelled) {
            throw new IllegalStateException("cannot ship this order");
        }
        shipped = true;
        events.add("shipped " + trackingNumber);
    }

    public void deliver() {
        if (!shipped || delivered) {
            throw new IllegalStateException("cannot deliver this order");
        }
        delivered = true;
        events.add("delivered");
    }

    public void cancel() {
        if (shipped || cancelled) {
            throw new IllegalStateException("cannot cancel this order");
        }
        cancelled = true;
        events.add(paid ? "cancelled, refund issued" : "cancelled");
    }

    public List<String> events() {
        return List.copyOf(events);
    }
}
