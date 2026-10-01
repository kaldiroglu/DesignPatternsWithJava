package dev.kaldiroglu.dp.behavioral.command.account.problem;

import dev.kaldiroglu.dp.behavioral.command.account.domain.InsufficientFundsException;
import dev.kaldiroglu.dp.behavioral.command.account.domain.Money;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Stage two: <b>the account keeps a history.</b>
 * <p>
 * A real improvement on stage one. The account keeps every operation it has performed, so
 * a teller can undo as far back as they like, redo what they undid, and hand the auditors
 * a journal of the day. Nothing is lost, and the names are an enum, so a typo no longer
 * compiles.
 * <p>
 * What it costs is the account. It now has eight public operations and three of them are
 * banking; the other five are a family of helper methods around them — undo, redo, the
 * journal, and the questions a screen asks before it enables a button. And every operation
 * lives in
 * three places: the method that performs it, a branch in {@link #undo()} and a branch in
 * {@link #redo()}. A third operation is an edit to all three.
 */
public final class HistoryAccount {

    private record Entry(Kind kind, Money amount) { }

    private final String owner;
    private Money balance;
    private final Deque<Entry> done = new ArrayDeque<>();
    private final Deque<Entry> undone = new ArrayDeque<>();
    private final List<String> journal = new ArrayList<>();

    public HistoryAccount(String owner, Money opening) {
        this.owner = owner;
        this.balance = opening;
    }

    public Money balance() {
        return balance;
    }

    public void deposit(Money amount) {
        balance = balance.plus(amount);
        record(new Entry(Kind.DEPOSIT, amount));
    }

    public void withdraw(Money amount) {
        if (balance.isLessThan(amount)) {
            throw new InsufficientFundsException(owner, balance, amount);
        }
        balance = balance.minus(amount);
        record(new Entry(Kind.WITHDRAW, amount));
    }

    public void undo() {
        if (done.isEmpty()) {
            return;
        }
        Entry last = done.pop();
        switch (last.kind()) {
            case DEPOSIT -> balance = balance.minus(last.amount());
            case WITHDRAW -> balance = balance.plus(last.amount());
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
            case DEPOSIT -> balance = balance.plus(next.amount());
            case WITHDRAW -> balance = balance.minus(next.amount());
        }
        done.push(next);
        journal.add("redo " + describe(next));
    }

    public boolean canUndo() {
        return !done.isEmpty();
    }

    public boolean canRedo() {
        return !undone.isEmpty();
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
        return entry.kind().name().toLowerCase() + " " + entry.amount();
    }
}
