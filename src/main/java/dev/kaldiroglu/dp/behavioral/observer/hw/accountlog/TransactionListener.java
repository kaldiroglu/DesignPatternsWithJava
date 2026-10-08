package dev.kaldiroglu.dp.behavioral.observer.hw.accountlog;

/** The <b>Observer</b>: told about every change to an account's balance. */
public interface TransactionListener {

    void balanceChanged(Account account, int amount, int newBalance);
}
