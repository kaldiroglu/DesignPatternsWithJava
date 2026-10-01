package dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.domain.transaction;


import org.javaturk.dp.ch08.command.account.account2.pattern.domain.Account;
import org.javaturk.dp.ch08.command.account.account2.pattern.domain.Money;
import org.javaturk.dp.ch08.command.account.account2.pattern.ex.IllegalMoneyException;
import org.javaturk.dp.ch08.command.account.account2.pattern.ex.InsufficientBalanceException;

public class Deposit extends AbstractTransaction {
    private static final String activityName = "DEPOSIT";

    private Deposit(Money amount) {
        super(activityName, amount);
    }

    public static Deposit of(Money amount) {
        return new Deposit(amount);
    }

    @Override
    public void proceed(Account source) throws InsufficientBalanceException, IllegalMoneyException {
        super.proceed(source);
        Money sourceBalance = source.balance();
        Money newSourceBalance = sourceBalance.deposit(amount); // If so go ahead and deposit amount and get the new Money object
        source.newBalance(newSourceBalance); // Set it as new balance on the source account
    }
}
