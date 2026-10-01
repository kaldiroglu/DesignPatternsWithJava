package dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.domain.transaction;


import dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.domain.Account;
import dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.ex.IllegalMoneyException;
import dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.ex.InsufficientBalanceException;

public interface Transaction {

    public String name();

    public void proceed(Account source) throws InsufficientBalanceException, IllegalMoneyException;

    void processed();
}
