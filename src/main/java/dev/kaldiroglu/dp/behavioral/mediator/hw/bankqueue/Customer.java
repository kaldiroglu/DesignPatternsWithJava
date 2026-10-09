package dev.kaldiroglu.dp.behavioral.mediator.hw.bankqueue;

/** A <b>Colleague</b>: a customer. It knows the queue manager, not the tellers. */
public final class Customer {

    private final String name;
    private final QueueManager manager;
    private int number;

    public Customer(String name, QueueManager manager) {
        this.name = name;
        this.manager = manager;
    }

    public void arrive() {
        manager.arrive(this);
    }

    public String name() {
        return name;
    }

    public int number() {
        return number;
    }

    void setNumber(int number) {
        this.number = number;
    }
}
