package dev.kaldiroglu.dp.behavioral.observer.hw.accountlog;

import java.util.ArrayList;
import java.util.List;

/**
 * Homework 1: the account from the Command deck, as a <b>Subject</b>.
 * <p>
 * The account does banking and tells its listeners after each change. It does not create
 * transaction records; that is the listener's job. The account stays as small as it was in
 * the Command deck's solution.
 */
public final class Account {

    private final String owner;
    private int balance;
    private final List<TransactionListener> listeners = new ArrayList<>();

    public Account(String owner, int balance) {
        this.owner = owner;
        this.balance = balance;
    }

    public void addListener(TransactionListener listener) {
        listeners.add(listener);
    }

    public String owner() {
        return owner;
    }

    public void deposit(int amount) {
        balance += amount;
        notifyListeners(amount);
    }

    public void withdraw(int amount) {
        if (amount > balance) {
            throw new IllegalArgumentException(owner + " cannot withdraw " + amount);
        }
        balance -= amount;
        notifyListeners(-amount);
    }

    private void notifyListeners(int amount) {
        for (TransactionListener listener : List.copyOf(listeners)) {
            listener.balanceChanged(this, amount, balance);
        }
    }
}
