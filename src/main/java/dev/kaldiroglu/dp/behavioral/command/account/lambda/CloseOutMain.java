package dev.kaldiroglu.dp.behavioral.command.account.lambda;

import dev.kaldiroglu.dp.behavioral.command.account.domain.Account;
import dev.kaldiroglu.dp.behavioral.command.account.domain.Money;
import dev.kaldiroglu.dp.behavioral.command.account.solution.Teller;

import static dev.kaldiroglu.dp.behavioral.command.account.lambda.Transactions.closeOut;

/** Close out an account, then undo: the command gives back what it took. */
public final class CloseOutMain {

    public static void main(String[] args) {
        Account closed = new Account("Deniz", Money.of("750.00"));
        Teller teller = new Teller();
        teller.perform(closeOut(closed));
        System.out.println("Close out Deniz: balance " + closed.balance());
        teller.undo();
        System.out.println("Undo: balance " + closed.balance()
                + " (the command remembered what it took)");
    }
}
