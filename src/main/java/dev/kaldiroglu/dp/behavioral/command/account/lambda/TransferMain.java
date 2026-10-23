package dev.kaldiroglu.dp.behavioral.command.account.lambda;

import dev.kaldiroglu.dp.behavioral.command.account.domain.Account;
import dev.kaldiroglu.dp.behavioral.command.account.domain.Money;
import dev.kaldiroglu.dp.behavioral.command.account.solution.Teller;

import static dev.kaldiroglu.dp.behavioral.command.account.lambda.Transactions.transfer;

/**
 * The transfer from the problem again, made of lambdas. The teller is the one from
 * {@code solution}, and one undo takes back the whole transfer.
 */
public final class TransferMain {

    public static void main(String[] args) {
        Account deniz = new Account("Deniz", Money.of("1000.00"));
        Account emre = new Account("Emre", Money.of("0.00"));
        Teller teller = new Teller();

        teller.perform(transfer(deniz, emre, Money.of("300.00")));
        System.out.println("Transfer 300 from Deniz to Emre. Deniz " + deniz.balance()
                + ", Emre " + emre.balance());
        teller.undo();
        System.out.println("Undo pressed once. Deniz " + deniz.balance() + ", Emre " + emre.balance());
        System.out.println("Undo took back the whole transfer.");
    }
}
