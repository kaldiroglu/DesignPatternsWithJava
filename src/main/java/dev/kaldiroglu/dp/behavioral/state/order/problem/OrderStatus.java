package dev.kaldiroglu.dp.behavioral.state.order.problem;

/**
 * Stage three: <b>each status is an enum constant with its own behavior.</b>
 * <p>
 * The best of the three. The rules of one status are in one place: read {@code SHIPPED} to
 * see what a shipped order may do. Each constant returns the next status, so the
 * transitions are in the statuses. A new status is one new constant.
 * <p>
 * The limit: an enum constant is one object shared by every order. It cannot hold one
 * order's data. A shipped order now has a tracking number and a count of failed delivery
 * attempts, and both have to live in {@link EnumOrder} — fields that mean something only
 * while the order is shipped, and that every transition must remember to set or reset.
 * {@code PAID.ship} forgets to reset the count.
 */
public enum OrderStatus {

    PLACED {
        @Override
        OrderStatus pay(EnumOrder order) {
            order.event("paid");
            return PAID;
        }

        @Override
        OrderStatus cancel(EnumOrder order) {
            order.event("cancelled");
            return CANCELLED;
        }
    },
    PAID {
        @Override
        OrderStatus ship(EnumOrder order, String trackingNumber) {
            order.trackingNumber = trackingNumber;   // the attempt count is not reset
            order.event("shipped " + trackingNumber);
            return SHIPPED;
        }

        @Override
        OrderStatus cancel(EnumOrder order) {
            order.event("cancelled, refund issued");
            return CANCELLED;
        }
    },
    SHIPPED {
        @Override
        OrderStatus failDelivery(EnumOrder order) {
            order.failedAttempts++;
            order.event("delivery attempt " + order.failedAttempts + " failed");
            if (order.failedAttempts == EnumOrder.MAX_ATTEMPTS) {
                order.event("back to the warehouse");
                return PAID;
            }
            return SHIPPED;
        }

        @Override
        OrderStatus deliver(EnumOrder order) {
            order.event("delivered");
            return DELIVERED;
        }
    },
    DELIVERED,
    CANCELLED;

    OrderStatus pay(EnumOrder order) {
        throw reject("pay");
    }

    OrderStatus ship(EnumOrder order, String trackingNumber) {
        throw reject("ship");
    }

    OrderStatus failDelivery(EnumOrder order) {
        throw reject("record a failed delivery for");
    }

    OrderStatus deliver(EnumOrder order) {
        throw reject("deliver");
    }

    OrderStatus cancel(EnumOrder order) {
        throw reject("cancel");
    }

    private IllegalStateException reject(String action) {
        return new IllegalStateException("cannot " + action + " a " + name().toLowerCase() + " order");
    }
}
