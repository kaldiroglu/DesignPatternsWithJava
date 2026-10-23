package dev.kaldiroglu.dp.behavioral.command.account.problem;

import dev.kaldiroglu.dp.behavioral.command.account.domain.Account;
import dev.kaldiroglu.dp.behavioral.command.account.domain.Money;

/** Stage three: the teller transfers 300, and one undo takes back only half of it. */
public final class TransferUndoMain {

    public static void main(String[] args) {
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
