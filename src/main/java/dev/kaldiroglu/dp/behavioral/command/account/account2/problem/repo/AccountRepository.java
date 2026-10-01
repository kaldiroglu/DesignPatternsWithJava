package dev.kaldiroglu.dp.behavioral.command.account.account2.problem.repo;

import dev.kaldiroglu.dp.behavioral.command.account.account2.problem.Account;
import dev.kaldiroglu.dp.behavioral.command.account.account2.problem.ex.AccountNotFoundException;

public interface AccountRepository {

    public Account load(int id) throws AccountNotFoundException;

    public void update(Account account);
}
