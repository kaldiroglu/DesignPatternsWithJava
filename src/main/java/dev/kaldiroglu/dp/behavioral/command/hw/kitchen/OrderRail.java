package dev.kaldiroglu.dp.behavioral.command.hw.kitchen;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * The <b>Invoker</b>: the rail of tickets above the kitchen pass.
 * <p>
 * The homework's question is who knows whether an order can still be cancelled. The answer
 * is this class, and nobody else: an order on the rail has not started, and an order off
 * it has. Cancelling is removing a ticket that has not run — the order needs no undo,
 * because nothing has happened yet that would need reversing. A dish already cooked is not
 * undone; it is a refund, which is a different request.
 */
public final class OrderRail {

    private final Deque<Order> waiting = new ArrayDeque<>();

    public void place(Order order) {
        waiting.addLast(order);
    }

    /** Answers false when the kitchen has already started it. */
    public boolean cancel(Order order) {
        return waiting.remove(order);
    }

    public int waiting() {
        return waiting.size();
    }

    /** The kitchen takes the oldest ticket. */
    public void cookNext() {
        Order next = waiting.pollFirst();
        if (next != null) {
            next.execute();
        }
    }
}
