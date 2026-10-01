package dev.kaldiroglu.dp.behavioral.command.account.problem;

import dev.kaldiroglu.dp.behavioral.command.account.domain.Account;
import dev.kaldiroglu.dp.behavioral.command.account.domain.Money;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Stage three: <b>the history moves out of the account.</b>
 * <p>
 * The best of the three, and where a careful team lands. The account is back to doing
 * banking and nothing else — {@code domain.Account} has no idea it can be undone. The
 * teller performs each operation, writes down what it did, and branches on what it wrote
 * down to reverse it. Undo, redo and the journal all work, across any number of accounts.
 * <p>
 * Then the bank adds transfers. A transfer is a withdrawal and a deposit, both already
 * written and both already undoable, so {@link #transfer} reuses them — which is exactly
 * what a good developer should do. It records two entries. The teller presses Undo once,
 * and Undo takes back the deposit and leaves the withdrawal: the money has left one
 * account and arrived nowhere.
 * <p>
 * The fix inside this design is a third {@link Kind}, an entry field that only transfers
 * use, and a branch in both switches. The request was never a thing the teller could hold;
 * it was a name and an amount that the teller has to interpret again every time it looks
 * back.
 */
public final class Teller {

    private record Entry(Kind kind, Account account, Money amount) { }

    private final Deque<Entry> done = new ArrayDeque<>();
    private final Deque<Entry> undone = new ArrayDeque<>();
    private final List<String> journal = new ArrayList<>();

    public void deposit(Account account, Money amount) {
        account.deposit(amount);
        record(new Entry(Kind.DEPOSIT, account, amount));
    }

    public void withdraw(Account account, Money amount) {
        account.withdraw(amount);
        record(new Entry(Kind.WITHDRAW, account, amount));
    }

    /** Added later, by reusing the two operations that already work. */
    public void transfer(Account from, Account to, Money amount) {
        withdraw(from, amount);
        deposit(to, amount);
    }

    public void undo() {
        if (done.isEmpty()) {
            return;
        }
        Entry last = done.pop();
        switch (last.kind()) {
            case DEPOSIT -> last.account().withdraw(last.amount());
            case WITHDRAW -> last.account().deposit(last.amount());
        }
        undone.push(last);
        journal.add("undo " + describe(last));
    }

    public void redo() {
        if (undone.isEmpty()) {
            return;
        }
        Entry next = undone.pop();
        switch (next.kind()) {
            case DEPOSIT -> next.account().deposit(next.amount());
            case WITHDRAW -> next.account().withdraw(next.amount());
        }
        done.push(next);
        journal.add("redo " + describe(next));
    }

    public List<String> journal() {
        return List.copyOf(journal);
    }

    private void record(Entry entry) {
        done.push(entry);
        undone.clear();
        journal.add(describe(entry));
    }

    private String describe(Entry entry) {
        return entry.kind().name().toLowerCase() + " " + entry.amount() + " "
                + entry.account().owner();
    }
}
