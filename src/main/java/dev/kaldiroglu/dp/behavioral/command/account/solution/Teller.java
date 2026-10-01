package dev.kaldiroglu.dp.behavioral.command.account.solution;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * The <b>Invoker</b>: performs transactions, keeps them, and takes them back.
 * <p>
 * Compare {@code problem.Teller}. That teller had a method per operation and a switch per
 * direction, and it remembered names and amounts that it had to interpret again whenever
 * it looked back. This one has no operation of its own. It is handed a transaction, runs
 * it, and keeps the object — so undo is "take the last one off the pile and ask it", and
 * redo is "ask it again".
 * <p>
 * Nothing in this class names an operation. Deposits, withdrawals, transfers, close-outs,
 * and whatever the bank invents next are all one type to it.
 */
public final class Teller {

    private final Deque<Transaction> done = new ArrayDeque<>();
    private final Deque<Transaction> undone = new ArrayDeque<>();
    private final List<String> journal = new ArrayList<>();

    public void perform(Transaction transaction) {
        transaction.execute();
        done.push(transaction);
        undone.clear();
        journal.add(transaction.description());
    }

    public void undo() {
        if (done.isEmpty()) {
            return;
        }
        Transaction last = done.pop();
        last.undo();
        undone.push(last);
        journal.add("undo " + last.description());
    }

    public void redo() {
        if (undone.isEmpty()) {
            return;
        }
        Transaction next = undone.pop();
        next.execute();
        done.push(next);
        journal.add("redo " + next.description());
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
}
