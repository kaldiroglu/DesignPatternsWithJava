package dev.kaldiroglu.dp.behavioral.command.account.solution;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Requests made during the day and carried out at night.
 * <p>
 * A method call happens when it is made. A transaction is an object, so it can be made at
 * ten in the morning and executed at midnight — and in between it is just something in a
 * queue. This is the "queue requests" half of GoF's intent, and it needs nothing from the
 * transactions that undo did not already need: they were complete when they were built.
 * <p>
 * The night run goes through an ordinary {@link Teller}, so everything done tonight is in
 * the journal and can be undone tomorrow like anything else.
 */
public final class StandingOrders {

    private final Queue<Transaction> tonight = new ArrayDeque<>();

    public void schedule(Transaction transaction) {
        tonight.add(transaction);
    }

    public int pending() {
        return tonight.size();
    }

    /** Carry out everything scheduled, in the order it was scheduled. */
    public void runThrough(Teller teller) {
        while (!tonight.isEmpty()) {
            teller.perform(tonight.poll());
        }
    }
}
