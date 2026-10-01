package dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.domain.transaction;

import org.javaturk.dp.ch08.command.account.account2.pattern.domain.Account;
import org.javaturk.dp.ch08.command.account.account2.pattern.domain.Money;
import org.javaturk.dp.ch08.command.account.account2.pattern.ex.IllegalMoneyException;
import org.javaturk.dp.ch08.command.account.account2.pattern.ex.InsufficientBalanceException;

public class Withdraw extends AbstractTransaction {
    private static final String activityName = "WITHDRAW";

    private Withdraw(Money amount) {
        super(activityName, amount);
    }

    public static Withdraw of(Money amount) {
        return new Withdraw(amount);
    }

    @Override
    public void proceed(Account sourceAccount) throws InsufficientBalanceException, IllegalMoneyException {
        super.proceed(sourceAccount);
        Money sourceBalance = sourceAccount.balance();
        if (sourceBalance.withdrawable(amount)) { // Check whether amount can be withdrawn
            Money newSourceBalance = sourceBalance.withdraw(amount); // If so go ahead and withdraw amount and get the new Money object
            sourceAccount.newBalance(newSourceBalance); // Set it as new balance on the source account
        }
    }
}
