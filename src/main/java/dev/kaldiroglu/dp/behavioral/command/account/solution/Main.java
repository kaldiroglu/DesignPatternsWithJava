package dev.kaldiroglu.dp.behavioral.command.account.solution;

import dev.kaldiroglu.dp.behavioral.command.account.domain.Account;
import dev.kaldiroglu.dp.behavioral.command.account.domain.Money;

/**
 * Runs the transfer from the problem again, with each request an object, and undoes it as
 * one. Then shows standing orders: requests made in the morning and run at night.
 */
public class Main {

    public static void main(String[] args) {
        Account deniz = new Account("Deniz", Money.of("1000.00"));
        Account emre = new Account("Emre", Money.of("0.00"));
        Teller teller = new Teller();

        teller.perform(new Transfer(deniz, emre, Money.of("300.00")));
        System.out.println("Transfer 300 from Deniz to Emre. Deniz " + deniz.balance()
                + ", Emre " + emre.balance());
        teller.undo();
        System.out.println("Undo pressed once. Deniz " + deniz.balance() + ", Emre " + emre.balance());
        System.out.println("Undo took back the whole transfer.");

        Account closed = new Account("Deniz", Money.of("750.00"));
        teller.perform(new CloseOut(closed));
        System.out.println("Close out Deniz: balance " + closed.balance());
        teller.undo();
        System.out.println("Undo: balance " + closed.balance()
                + " (the command remembered what it took)");

        standingOrders();
    }

    /** Three orders are made in the morning. Nothing happens until the night run. */
    private static void standingOrders() {
        System.out.println();
        System.out.println("Standing orders");
        Account deniz = new Account("Deniz", Money.of("1000.00"));
        Account landlord = new Account("Landlord", Money.of("0.00"));
        Account savings = new Account("Savings", Money.of("0.00"));

        StandingOrders orders = new StandingOrders();
        orders.schedule(new Transfer(deniz, landlord, Money.of("400.00")));
        orders.schedule(new Transfer(deniz, savings, Money.of("100.00")));
        orders.schedule(new Deposit(savings, Money.of("5.00")));
        System.out.println("Morning: " + orders.pending() + " orders waiting. Deniz still has "
                + deniz.balance());

        Teller nightRun = new Teller();
        orders.runThrough(nightRun);
        System.out.println("Night: Deniz " + deniz.balance() + ", Landlord " + landlord.balance()
                + ", Savings " + savings.balance() + ". Orders waiting: " + orders.pending());
        System.out.println("Night journal: " + nightRun.journal());

        nightRun.undo();
        System.out.println("Next morning, undo the last order: Savings " + savings.balance());
    }
}
