package dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.repo;

import dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.domain.Account;
import dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.ex.AccountNotFoundException;

public interface AccountRepository {

    public Account load(int id) throws AccountNotFoundException;

    public void update(Account account);
}
