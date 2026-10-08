package dev.kaldiroglu.dp.behavioral.state.order.problem;

import java.util.ArrayList;
import java.util.List;

/**
 * Stage two: <b>one status field, and a switch on it in every method.</b>
 * <p>
 * A real improvement on stage one. The order has exactly one status, so an order that is
 * both cancelled and shipped cannot exist. The switches have no {@code default}, so a new
 * status that is missing from a switch does not compile.
 * <p>
 * What it costs: the rules of one status are spread over every method. To read what a paid
 * order may do, you read five switches. A new status is an edit to every one of them.
 */
public final class SwitchingOrder {

    private Status status = Status.PLACED;
    private final List<String> events = new ArrayList<>();

    public void pay() {
        status = switch (status) {
            case PLACED -> record(Status.PAID, "paid");
            case PAID, SHIPPED, DELIVERED, CANCELLED -> reject("pay");
        };
    }

    public void ship(String trackingNumber) {
        status = switch (status) {
            case PAID -> record(Status.SHIPPED, "shipped " + trackingNumber);
            case PLACED, SHIPPED, DELIVERED, CANCELLED -> reject("ship");
        };
    }

    public void deliver() {
        status = switch (status) {
            case SHIPPED -> record(Status.DELIVERED, "delivered");
            case PLACED, PAID, DELIVERED, CANCELLED -> reject("deliver");
        };
    }

    public void cancel() {
        status = switch (status) {
            case PLACED -> record(Status.CANCELLED, "cancelled");
            case PAID -> record(Status.CANCELLED, "cancelled, refund issued");
            case SHIPPED, DELIVERED, CANCELLED -> reject("cancel");
        };
    }

    public Status status() {
        return status;
    }

    public List<String> events() {
        return List.copyOf(events);
    }

    private Status record(Status next, String event) {
        events.add(event);
        return next;
    }

    private Status reject(String action) {
        throw new IllegalStateException("cannot " + action + " a " + status.name().toLowerCase() + " order");
    }
}
