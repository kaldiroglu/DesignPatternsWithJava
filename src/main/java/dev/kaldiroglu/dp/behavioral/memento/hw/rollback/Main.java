package dev.kaldiroglu.dp.behavioral.memento.hw.rollback;

import java.util.List;

/**
 * Runs two batches of transfers to Can. The first fails at Ayse's transfer, and every
 * account gets its balance back; the second succeeds.
 */
public final class Main {

    public static void main(String[] args) {
        Account ali = new Account("Ali", 100);
        Account ayse = new Account("Ayse", 50);
        Account can = new Account("Can", 0);
        List<Account> accounts = List.of(ali, ayse, can);

        Batch failing = new Batch();
        failing.add(ali, can, 80);
        failing.add(ayse, can, 70);
        System.out.println("Batch 1: " + failing.run(accounts) + " -> " + ali + ", " + ayse + ", " + can);

        Batch working = new Batch();
        working.add(ali, can, 80);
        working.add(ayse, can, 50);
        System.out.println("Batch 2: " + working.run(accounts) + " -> " + ali + ", " + ayse + ", " + can);
    }
}
