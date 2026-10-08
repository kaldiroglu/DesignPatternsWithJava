package dev.kaldiroglu.dp.behavioral.state.order.problem;

/** Stage two's statuses. One field, so an order has exactly one of them. */
public enum Status {
    PLACED, PAID, SHIPPED, DELIVERED, CANCELLED
}
