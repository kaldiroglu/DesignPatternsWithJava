package dev.kaldiroglu.dp.behavioral.command.account.solution;

import dev.kaldiroglu.dp.behavioral.command.account.domain.Account;
import dev.kaldiroglu.dp.behavioral.command.account.domain.Money;

/** Runs the transfer from the problem again, with each request an object, and undoes it as one. */
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

        Account landlord = new Account("Landlord", Money.of("0.00"));
        StandingOrders orders = new StandingOrders();
        orders.schedule(new Transfer(deniz, landlord, Money.of("400.00")));
        System.out.println("Standing orders waiting tonight: " + orders.pending()
                + ". Deniz still holds " + deniz.balance());
        orders.runThrough(teller);
        System.out.println("After the night run: Deniz " + deniz.balance()
                + ", Landlord " + landlord.balance());
        System.out.println("Journal: " + teller.journal());
    }
}
