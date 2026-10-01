package dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.app;

import org.javaturk.dp.ch08.command.account.account2.pattern.domain.AccountService;
import org.javaturk.dp.ch08.command.account.account2.pattern.domain.Money;
import org.javaturk.dp.ch08.command.account.account2.pattern.domain.transaction.TransactionTypes;
import org.javaturk.dp.ch08.command.account.account2.pattern.ex.AccountNotFoundException;
import org.javaturk.dp.ch08.command.account.account2.pattern.ex.IllegalMoneyException;
import org.javaturk.dp.ch08.command.account.account2.pattern.domain.Account;
import org.javaturk.dp.ch08.command.account.account2.pattern.ex.InsufficientBalanceException;
import org.javaturk.dp.ch08.command.account.account2.pattern.repo.AccountRepository;

import java.math.BigDecimal;
import java.util.logging.Logger;

public class AccountAppService {
    private AccountRepository accountRepository;
    private Logger log;
    private AccountService accountService;

    /**
     * Problems to be corrected:
     *  - What if an account does not exist for given id
     *
     */
    public void doTransaction(int accountId, TransactionTypes action, double amount) throws IllegalMoneyException, InsufficientBalanceException, AccountNotFoundException {
        // First log
        log.info(action + " : " + amount + " for account id: " + accountId);

        // Then load Account
        Account account = accountRepository.load(accountId);

        // Create Money object
        Money money = Money.of(BigDecimal.valueOf(amount));

        // Proceed to transaction
        if (action.equals(TransactionTypes.DEPOSIT))
            accountService.deposit(account, money);
        else if (action.equals(TransactionTypes.WITHDRAW))
            accountService.withdraw(account, money);

        // Then update Account
        accountRepository.update(account);
    }
}

