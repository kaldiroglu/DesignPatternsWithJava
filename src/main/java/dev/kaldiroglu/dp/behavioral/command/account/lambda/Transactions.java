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
 * three lambdas share a one-element array. That array is a small class written by hand —
 * the reason {@code solution.CloseOut} is a class.
 */
public final class Transactions {

    private Transactions() {
    }

    // Each factory passes three lambdas to LambdaTransaction, in this order: what execute()
    // does, what undo() does, and the description for the journal. The undo of a deposit is
    // a withdrawal, so a deposit's second lambda withdraws.

    public static Transaction deposit(Account account, Money amount) {
        return new LambdaTransaction(
                () -> account.deposit(amount),          // execute
                () -> account.withdraw(amount),         // undo: take the deposit back out
                () -> "deposit " + amount + " " + account.owner());
    }

    public static Transaction withdraw(Account account, Money amount) {
        return new LambdaTransaction(
                () -> account.withdraw(amount),         // execute
                () -> account.deposit(amount),          // undo: put the money back
                () -> "withdraw " + amount + " " + account.owner());
    }

    /**
     * The withdrawal runs first. If it fails, it throws before anything has changed, so the
     * transfer is still all or nothing; a deposit cannot fail.
     */
    public static Transaction transfer(Account from, Account to, Money amount) {
        return new LambdaTransaction(
                () -> {                                 // execute
                    from.withdraw(amount);
                    to.deposit(amount);
                },
                () -> {                                 // undo: the same steps reversed
                    to.withdraw(amount);
                    from.deposit(amount);
                },
                () -> "transfer " + amount + " " + from.owner() + " -> " + to.owner());
    }

    public static Transaction closeOut(Account account) {
        Money[] taken = {Money.ZERO};           // shared by the three lambdas: the state
        return new LambdaTransaction(
                () -> {                                 // execute
                    taken[0] = account.balance();
                    account.withdraw(taken[0]);
                },
                () -> account.deposit(taken[0]),        // undo: give back what it took
                () -> "close out " + account.owner() + ", paid " + taken[0]);
    }
}
