package dev.kaldiroglu.dp.behavioral.command.account.solution;

import dev.kaldiroglu.dp.behavioral.command.account.domain.Account;
import dev.kaldiroglu.dp.behavioral.command.account.domain.Money;

/**
 * A <b>ConcreteCommand</b> whose amount is not known until it runs: pay out the whole
 * balance.
 * <p>
 * The request carries no amount — "pay out whatever is there" — so undo cannot be computed
 * from the request. The command has to remember what it actually took. This is GoF
 * implementation issue 2 (supporting undo and redo): a command may need to store "any
 * original values in the receiver that can change as a result of handling the request".
 * <p>
 * In {@code problem.Teller} this would be a third kind, an entry field that only one kind
 * fills in, and a branch in two switches. Here it is one field, in the only class that
 * needs it.
 */
public final class CloseOut implements Transaction {

    private final Account account;
    private Money taken = Money.ZERO;      // learned when it runs, needed to undo

    public CloseOut(Account account) {
        this.account = account;
    }

    @Override
    public void execute() {
        taken = account.balance();
        account.withdraw(taken);
    }

    @Override
    public void undo() {
        account.deposit(taken);
    }

    @Override
    public String description() {
        return "close out " + account.owner() + ", paid " + taken;
    }
}
