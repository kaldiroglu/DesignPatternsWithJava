package dev.kaldiroglu.dp.behavioral.command.account.account2.problem.repo;

import org.javaturk.dp.ch08.command.account.account2.problem.Account;
import org.javaturk.dp.ch08.command.account.account2.problem.ex.AccountNotFoundException;

public interface AccountRepository {

    public Account load(int id) throws AccountNotFoundException;

    public void update(Account account);
}
