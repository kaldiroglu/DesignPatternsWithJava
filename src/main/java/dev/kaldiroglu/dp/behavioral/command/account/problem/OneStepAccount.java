package dev.kaldiroglu.dp.behavioral.command.account.problem;

import dev.kaldiroglu.dp.behavioral.command.account.domain.InsufficientFundsException;
import dev.kaldiroglu.dp.behavioral.command.account.domain.Money;

/**
 * Stage one: <b>the account remembers its last move.</b>
 * <p>
 * The teller screen gets an Undo button, and the quickest place to put undo is on the
 * account itself. So the account writes down what it last did and how much, and
 * {@link #undo()} branches on that to do the opposite.
 * <p>
 * It works, and for a single slip it is all a teller needs. What it costs:
 * <ul>
 *   <li>One step only. A second undo finds nothing to reverse, because the account
 *       remembers one operation and the first undo erased it.</li>
 *   <li>Two of the account's three fields are not about money. They are bookkeeping for a
 *       screen the account should never have heard of.</li>
 *   <li>Every new operation is an edit to {@code undo()} as well as a new method.</li>
 * </ul>
 */
public final class OneStepAccount {

    private final String owner;
    private Money balance;

    private String lastKind = "NONE";        // what the last operation was
    private Money lastAmount = Money.ZERO;   // and how much it moved

    public OneStepAccount(String owner, Money opening) {
        this.owner = owner;
        this.balance = opening;
    }

    public Money balance() {
        return balance;
    }

    public void deposit(Money amount) {
        balance = balance.plus(amount);
        lastKind = "DEPOSIT";
        lastAmount = amount;
    }

    public void withdraw(Money amount) {
        if (balance.isLessThan(amount)) {
            throw new InsufficientFundsException(owner, balance, amount);
        }
        balance = balance.minus(amount);
        lastKind = "WITHDRAW";
        lastAmount = amount;
    }

    public void undo() {
        switch (lastKind) {
            case "DEPOSIT" -> balance = balance.minus(lastAmount);
            case "WITHDRAW" -> balance = balance.plus(lastAmount);
            default -> { }                    // nothing to undo
        }
        lastKind = "NONE";
    }
}
