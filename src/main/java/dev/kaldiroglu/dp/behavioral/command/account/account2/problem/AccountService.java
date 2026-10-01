package dev.kaldiroglu.dp.behavioral.command.account.account2.problem;

import org.javaturk.dp.ch08.command.account.account2.problem.ex.*;
import org.javaturk.dp.ch08.command.account.account2.problem.repo.AccountRepository;

import java.util.logging.Logger;

public class AccountService {
    private AccountRepository accountRepository;
    private Logger log;

    public void changeBalance(int accountId, String action, double amount) throws NegativeAmountException, InsufficentBalanceException, AccountNotFoundException {
        log.info(action + " : " + amount + " for account id: " + accountId);
        Account account = accountRepository.load(accountId);

        if (amount < 0)
            throw new NegativeAmountException(amount);

        double balance = account.getBalance();

        if (action.equals("Deposit")) {
            balance += amount;
            account.setBalance(balance);
        } else if (action.equals("Withdraw")) {
            if (balance >= amount) {
                balance -= amount;
            } else {
                throw new InsufficentBalanceException(balance);
            }
        }
    }
}