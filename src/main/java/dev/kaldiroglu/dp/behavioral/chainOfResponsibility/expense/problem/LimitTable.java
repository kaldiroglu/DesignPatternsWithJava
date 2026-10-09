package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.problem;

import java.util.Map;
import java.util.TreeMap;

/**
 * Stage three: a table of limits, set up when the program starts.
 * <p>
 * No names in the code, no order fixed in a class: add a row and there is a new level.
 * The table finds the approver with the smallest limit that covers the amount. But the
 * table only knows amounts. When the manager spends 5,000, the table sends it to the
 * manager — who approves an expense of their own. The rule "not your own expense" belongs
 * to the approver, and the table has no place for it.
 */
public final class LimitTable {

    private final TreeMap<Integer, String> approvers = new TreeMap<>();

    public LimitTable add(String approver, int limit) {
        approvers.put(limit, approver);
        return this;
    }

    public String approve(Expense expense) {
        Map.Entry<Integer, String> row = approvers.ceilingEntry(expense.amount());
        if (row == null) {
            return "no one may approve it";
        }
        return "approved by " + row.getValue();
    }
}
