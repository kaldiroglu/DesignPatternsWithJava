package dev.kaldiroglu.dp.behavioral.state.order.problem;

import java.util.ArrayList;
import java.util.List;

/**
 * Stage three's order. It forwards every request to its {@link OrderStatus} constant, and it
 * also holds the data that belongs to one status only: the tracking number and the failed
 * attempts of a shipment.
 * <p>
 * The promise: a courier gets three delivery attempts. After three failures the parcel goes
 * back to the warehouse and can be shipped again — with three new attempts. Here the second
 * shipment starts at three, because nothing reset the count. Its first failure is counted as
 * attempt 4, which is never equal to 3, so the parcel never goes back again: the courier now
 * has no limit at all.
 */
public final class EnumOrder {

    static final int MAX_ATTEMPTS = 3;

    private OrderStatus status = OrderStatus.PLACED;
    String trackingNumber;      // means something only while SHIPPED
    int failedAttempts;         // means something only while SHIPPED
    private final List<String> events = new ArrayList<>();

    public void pay() {
        status = status.pay(this);
    }

    public void ship(String trackingNumber) {
        status = status.ship(this, trackingNumber);
    }

    public void failDelivery() {
        status = status.failDelivery(this);
    }

    public void deliver() {
        status = status.deliver(this);
    }

    public void cancel() {
        status = status.cancel(this);
    }

    public OrderStatus status() {
        return status;
    }

    void event(String event) {
        events.add(event);
    }

    public List<String> events() {
        return List.copyOf(events);
    }
}
