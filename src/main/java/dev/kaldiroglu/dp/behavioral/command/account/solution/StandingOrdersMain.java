package dev.kaldiroglu.dp.behavioral.command.account.solution;

import dev.kaldiroglu.dp.behavioral.command.account.domain.Account;
import dev.kaldiroglu.dp.behavioral.command.account.domain.Money;

/** Three orders are made in the morning. Nothing happens until the night run. */
public final class StandingOrdersMain {

    public static void main(String[] args) {
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
