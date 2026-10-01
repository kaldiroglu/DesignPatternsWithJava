package dev.kaldiroglu.dp.behavioral.command.account.domain;

import java.util.Objects;

/**
 * A bank account that does banking and nothing else.
 * <p>
 * This is the class the whole example is trying to protect. It has an owner, a balance and
 * the two operations a balance supports. It does not know what a teller is, what undo
 * means, or that anybody keeps a journal — and in {@code problem.OneStepAccount} and
 * {@code problem.HistoryAccount} you can see what it looks like when it is made to.
 * <p>
 * In the pattern's vocabulary this is the <b>Receiver</b>: the object that knows how to
 * carry out the request.
 */
public final class Account {

    private final String owner;
    private Money balance;

    public Account(String owner, Money opening) {
        this.owner = Objects.requireNonNull(owner);
        this.balance = Objects.requireNonNull(opening);
    }

    public String owner() {
        return owner;
    }

    public Money balance() {
        return balance;
    }

    public void deposit(Money amount) {
        balance = balance.plus(amount);
    }

    public void withdraw(Money amount) {
        if (balance.isLessThan(amount)) {
            throw new InsufficientFundsException(owner, balance, amount);
        }
        balance = balance.minus(amount);
    }
}
