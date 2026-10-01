package dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.domain.transaction;


import org.javaturk.dp.ch08.command.account.account2.pattern.domain.Account;
import org.javaturk.dp.ch08.command.account.account2.pattern.ex.IllegalMoneyException;
import org.javaturk.dp.ch08.command.account.account2.pattern.ex.InsufficientBalanceException;

public interface Transaction {

    public String name();

    public void proceed(Account source) throws InsufficientBalanceException, IllegalMoneyException;

    void processed();
}
