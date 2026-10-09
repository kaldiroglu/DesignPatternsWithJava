package dev.kaldiroglu.dp.behavioral.mediator.hw.bankqueue;

/**
 * Two tellers and three customers meet only through the queue manager, which pairs them
 * in arrival order.
 */
public final class Main {

    public static void main(String[] args) {
        QueueManager manager = new QueueManager();
        Teller first = new Teller("Teller 1", manager);
        Teller second = new Teller("Teller 2", manager);
        Customer ayse = new Customer("Ayse", manager);
        Customer mert = new Customer("Mert", manager);
        Customer deniz = new Customer("Deniz", manager);

        first.free();
        ayse.arrive();
        mert.arrive();
        deniz.arrive();
        second.free();
        first.free();

        manager.log().forEach(System.out::println);
    }
}
