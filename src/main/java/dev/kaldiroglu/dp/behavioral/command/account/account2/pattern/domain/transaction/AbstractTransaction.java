package dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.domain.transaction;

import org.javaturk.dp.ch08.command.account.account2.pattern.domain.Account;
import org.javaturk.dp.ch08.command.account.account2.pattern.domain.Money;
import org.javaturk.dp.ch08.command.account.account2.pattern.ex.IllegalMoneyException;
import org.javaturk.dp.ch08.command.account.account2.pattern.ex.InsufficientBalanceException;

import java.time.LocalDate;

public abstract class AbstractTransaction implements Transaction {
    private final String name;
    protected final LocalDate date;
    protected Account sourceAccount;
    protected final Money amount;
    protected boolean processed;

    public AbstractTransaction(String name,  Money amount) {
        this.name = name;
        date = LocalDate.now();
        this.amount = amount;
        processed = false;
    }

    public String name() {
        return name;
    }

    public Account source() {
        return sourceAccount;
    }

    public void proceed(Account sourceAccount) throws InsufficientBalanceException, IllegalMoneyException {
        this.sourceAccount = sourceAccount;
    }

    public void processed() {
        processed = true;
    }
}
