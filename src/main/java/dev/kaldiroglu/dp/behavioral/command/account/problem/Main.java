package dev.kaldiroglu.dp.behavioral.command.account.problem;

import dev.kaldiroglu.dp.behavioral.command.account.domain.Account;
import dev.kaldiroglu.dp.behavioral.command.account.domain.Money;

/** Runs the three stages, and shows the stage-three teller undoing only half of a transfer. */
public class Main {

    public static void main(String[] args) {
        OneStepAccount one = new OneStepAccount("Deniz", Money.of("1000.00"));
        one.deposit(Money.of("500.00"));
        one.withdraw(Money.of("200.00"));
        one.undo();
        one.undo();
        System.out.println("Stage one: deposit 500, withdraw 200, undo twice. Balance: "
                + one.balance() + " (the second undo did nothing)");

        HistoryAccount two = new HistoryAccount("Deniz", Money.of("1000.00"));
        two.deposit(Money.of("500.00"));
        two.withdraw(Money.of("200.00"));
        two.undo();
        two.undo();
        System.out.println("Stage two: the same steps. Balance: " + two.balance());
        System.out.println("Stage two journal: " + two.journal());

        Account deniz = new Account("Deniz", Money.of("1000.00"));
        Account emre = new Account("Emre", Money.of("0.00"));
        Teller teller = new Teller();
        teller.transfer(deniz, emre, Money.of("300.00"));
        System.out.println("Stage three: transfer 300 from Deniz to Emre. Deniz "
                + deniz.balance() + ", Emre " + emre.balance());
        teller.undo();
        System.out.println("Undo pressed once. Deniz " + deniz.balance() + ", Emre " + emre.balance());
        System.out.println("Undo took back only the deposit. "
                + Money.of("1000.00").minus(deniz.balance().plus(emre.balance()))
                + " lira left Deniz and arrived nowhere.");
        System.out.println("Journal: " + teller.journal());
    }
}
