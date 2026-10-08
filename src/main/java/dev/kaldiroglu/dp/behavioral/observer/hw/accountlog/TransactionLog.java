package dev.kaldiroglu.dp.behavioral.observer.hw.accountlog;

import java.util.ArrayList;
import java.util.List;

/**
 * A <b>ConcreteObserver</b>: creates a {@link Transaction} for every change it hears about.
 * <p>
 * The homework asked that the transaction objects be created by the listener, not by the
 * account. A failed withdrawal throws before any notification, so it is never recorded.
 */
public final class TransactionLog implements TransactionListener {

    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public void balanceChanged(Account account, int amount, int newBalance) {
        String kind = amount >= 0 ? "deposit" : "withdrawal";
        transactions.add(new Transaction(account.owner(), kind, Math.abs(amount), newBalance));
    }

    public List<Transaction> transactions() {
        return List.copyOf(transactions);
    }
}
