package dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.domain;

import dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.domain.transaction.Deposit;
import dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.domain.transaction.Withdraw;
import dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.ex.IllegalMoneyException;
import dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.ex.InsufficientBalanceException;

import javax.security.auth.login.AccountLockedException;

public class AccountService {

    public void deposit(Account sourceAccount, Money amount) throws InsufficientBalanceException, IllegalMoneyException {
        Deposit deposit = Deposit.of(amount);
        sourceAccount.changeBalance(deposit);
    }

    public void withdraw(Account sourceAccount, Money amount) throws InsufficientBalanceException, IllegalMoneyException {
        Withdraw withdraw = Withdraw.of(amount);
        sourceAccount.changeBalance(withdraw);
    }
}

/**
 *
 *    public void changeBalance(Account account, String action, double amount) throws NegativeAmountException, InsufficentBalanceException {
 *         log.info(action + " : " + amount + “ for account id: “ + id);
 *
 *         if (amount < 0)
 *             throw new NegativeAmountException(amount);
 *
 *         double balance = account.getBalance();
 *
 *         if (action.equals("Deposit")) {
 *             balance += amount;
 *             account.setBalance(balance);
 *         } else if (action.equals("Withdraw")) {
 *             if (balance >= amount) {
 *                 balance -= amount;
 *             } else {
 *                 throw new InsufficentBalanceException(balance);
 *             }
 *         }
 *     }
 * }
 *
 */
