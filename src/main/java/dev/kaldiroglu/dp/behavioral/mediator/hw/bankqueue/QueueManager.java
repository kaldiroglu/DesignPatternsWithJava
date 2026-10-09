package dev.kaldiroglu.dp.behavioral.mediator.hw.bankqueue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Homework 1: the <b>Mediator</b> of a bank branch.
 * <p>
 * Customers and tellers never look for each other. A customer who arrives takes a number
 * here; a teller who becomes free tells the manager. The manager keeps the waiting
 * customers and the idle tellers, and pairs them in arrival order.
 */
public final class QueueManager {

    private final Deque<Customer> waiting = new ArrayDeque<>();
    private final Deque<Teller> idle = new ArrayDeque<>();
    private final List<String> log = new ArrayList<>();
    private int nextNumber = 1;

    public void arrive(Customer customer) {
        customer.setNumber(nextNumber++);
        log.add(customer.name() + " takes number " + customer.number());
        if (idle.isEmpty()) {
            waiting.add(customer);
        } else {
            pair(idle.remove(), customer);
        }
    }

    public void tellerFree(Teller teller) {
        if (waiting.isEmpty()) {
            idle.add(teller);
            log.add(teller.name() + " waits for a customer");
        } else {
            pair(teller, waiting.remove());
        }
    }

    private void pair(Teller teller, Customer customer) {
        log.add(teller.name() + " calls number " + customer.number() + " (" + customer.name() + ")");
    }

    public List<String> log() {
        return List.copyOf(log);
    }
}
