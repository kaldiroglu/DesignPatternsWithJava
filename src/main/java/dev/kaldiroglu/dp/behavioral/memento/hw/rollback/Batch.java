package dev.kaldiroglu.dp.behavioral.memento.hw.rollback;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The <b>Caretaker</b>: runs a list of transfers as one unit.
 * <p>
 * Before the first transfer it takes a memento of every account. If any transfer fails, it
 * gives every account its memento back, so no half-done batch remains. It does not need to
 * know how to reverse each transfer.
 */
public final class Batch {

    public record Transfer(Account from, Account to, int amount) {
    }

    private final List<Transfer> transfers = new ArrayList<>();

    public void add(Account from, Account to, int amount) {
        transfers.add(new Transfer(from, to, amount));
    }

    /** Returns "done", or why it was rolled back. */
    public String run(List<Account> accounts) {
        Map<Account, Account.Saved> before = new LinkedHashMap<>();
        for (Account account : accounts) {
            before.put(account, account.save());
        }
        try {
            for (Transfer t : transfers) {
                t.from().withdraw(t.amount());
                t.to().deposit(t.amount());
            }
            return "done";
        } catch (IllegalStateException e) {
            before.forEach(Account::restore);
            return "rolled back: " + e.getMessage();
        }
    }
}
