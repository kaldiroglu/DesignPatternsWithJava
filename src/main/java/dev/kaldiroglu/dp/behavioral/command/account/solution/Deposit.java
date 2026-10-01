package dev.kaldiroglu.dp.behavioral.command.account.solution;

import dev.kaldiroglu.dp.behavioral.command.account.domain.Account;
import dev.kaldiroglu.dp.behavioral.command.account.domain.Money;

/**
 * A <b>ConcreteCommand</b>: money into one account.
 * <p>
 * It binds a receiver to an action — this account, a deposit of this much — and knows its
 * own opposite. Compare the {@code DEPOSIT} branch in {@code problem.Teller.undo()}: that
 * knowledge used to live in a switch in somebody else's class.
 */
public final class Deposit implements Transaction {

    private final Account account;
    private final Money amount;

    public Deposit(Account account, Money amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void execute() {
        account.deposit(amount);
    }

    @Override
    public void undo() {
        account.withdraw(amount);
    }

    @Override
    public String description() {
        return "deposit " + amount + " " + account.owner();
    }
}
