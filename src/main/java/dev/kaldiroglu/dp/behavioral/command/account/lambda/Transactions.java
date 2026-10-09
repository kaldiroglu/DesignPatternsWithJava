package dev.kaldiroglu.dp.behavioral.command.account.lambda;

import dev.kaldiroglu.dp.behavioral.command.account.domain.Account;
import dev.kaldiroglu.dp.behavioral.command.account.domain.Money;
import dev.kaldiroglu.dp.behavioral.command.account.solution.Transaction;

/**
 * The four transactions of {@code account.solution}, written as lambdas.
 * <p>
 * A deposit, a withdrawal and a transfer are easy: each undo is the opposite operation, and
 * everything it needs is known when the transaction is made. A close-out is not: it must
 * remember how much it took, to give it back on undo. A lambda cannot have a field, so the
 * two lambdas share a one-element array. That array is a small class written by hand —
 * the reason {@code solution.CloseOut} is a class.
 */
public final class Transactions {

    private Transactions() {
    }

    public static Transaction deposit(Account account, Money amount) {
        return new LambdaTransaction(
                () -> account.deposit(amount),
                () -> account.withdraw(amount),
                () -> "deposit " + amount + " " + account.owner());
    }

    public static Transaction withdraw(Account account, Money amount) {
        return new LambdaTransaction(
                () -> account.withdraw(amount),
                () -> account.deposit(amount),
                () -> "withdraw " + amount + " " + account.owner());
    }

    /**
     * The withdrawal runs first. If it fails, it throws before anything has changed, so the
     * transfer is still all or nothing; a deposit cannot fail.
     */
    public static Transaction transfer(Account from, Account to, Money amount) {
        return new LambdaTransaction(
                () -> {
                    from.withdraw(amount);
                    to.deposit(amount);
                },
                () -> {
                    to.withdraw(amount);
                    from.deposit(amount);
                },
                () -> "transfer " + amount + " " + from.owner() + " -> " + to.owner());
    }

    public static Transaction closeOut(Account account) {
        Money[] taken = {Money.ZERO};           // shared by the three lambdas: the state
        return new LambdaTransaction(
                () -> {
                    taken[0] = account.balance();
                    account.withdraw(taken[0]);
                },
                () -> account.deposit(taken[0]),
                () -> "close out " + account.owner() + ", paid " + taken[0]);
    }
}
