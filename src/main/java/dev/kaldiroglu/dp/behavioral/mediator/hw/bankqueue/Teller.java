package dev.kaldiroglu.dp.behavioral.mediator.hw.bankqueue;

/** A <b>Colleague</b>: a teller. It knows the queue manager, not the customers. */
public final class Teller {

    private final String name;
    private final QueueManager manager;

    public Teller(String name, QueueManager manager) {
        this.name = name;
        this.manager = manager;
    }

    public void free() {
        manager.tellerFree(this);
    }

    public String name() {
        return name;
    }
}
