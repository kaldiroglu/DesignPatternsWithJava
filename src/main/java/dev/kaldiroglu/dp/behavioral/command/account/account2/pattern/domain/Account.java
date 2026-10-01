package dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.domain;

import dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.domain.transaction.Transaction;
import dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.ex.IllegalMoneyException;
import dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.ex.InsufficientBalanceException;

public class Account {
    private Money balance;

    private Account(Money balance) {
        this.balance = balance;
    }

    public static Account withMoney(Money balance) {
        return new Account(balance);
    }

    public Money balance() {
        return balance;
    }

    public void newBalance(Money newBalance) {
        balance = newBalance;
    }

    // It uses the command pattern to manipulate the state of the account
    public void changeBalance(Transaction transaction) throws InsufficientBalanceException, IllegalMoneyException {
        transaction.proceed(this);
        transaction.processed();
    }

    // That's the regular way of manipulating the state of the account
    public void deposit(Money amount) {
        Money newBalance = balance.add(amount);
        balance = newBalance;
    }
}

