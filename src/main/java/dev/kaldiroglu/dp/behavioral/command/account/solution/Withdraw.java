package dev.kaldiroglu.dp.behavioral.command.account.solution;

import dev.kaldiroglu.dp.behavioral.command.account.domain.Account;
import dev.kaldiroglu.dp.behavioral.command.account.domain.Money;

/**
 * A <b>ConcreteCommand</b>: money out of one account.
 * <p>
 * If the account cannot cover it, {@code execute()} throws and nothing has changed — so a
 * failed withdrawal never reaches the teller's history, and can never be "undone" into
 * money the account did not have.
 */
public final class Withdraw implements Transaction {

    private final Account account;
    private final Money amount;

    public Withdraw(Account account, Money amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void execute() {
        account.withdraw(amount);
    }

    @Override
    public void undo() {
        account.deposit(amount);
    }

    @Override
    public String description() {
        return "withdraw " + amount + " " + account.owner();
    }
}
