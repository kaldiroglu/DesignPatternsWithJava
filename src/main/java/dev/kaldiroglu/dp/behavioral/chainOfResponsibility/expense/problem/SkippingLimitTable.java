package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.problem;

import java.util.Map;
import java.util.TreeMap;

/**
 * Stage three, repaired in the table: when the approver the table finds is the submitter,
 * the table moves on to the next higher limit.
 * <p>
 * The answers are now right. But the rule "not your own expense" belongs to the approvers,
 * and here the table applies it, so every new rule about one approver is another condition
 * in {@link #approve}. And the loop asks one approver after another until one may take the
 * expense: a chain, kept inside the table.
 */
public final class SkippingLimitTable {

    private final TreeMap<Integer, String> approvers = new TreeMap<>();

    public SkippingLimitTable add(String approver, int limit) {
        approvers.put(limit, approver);
        return this;
    }

    public String approve(Expense expense) {
        Map.Entry<Integer, String> row = approvers.ceilingEntry(expense.amount());
        while (row != null && row.getValue().equals(expense.submitter())) {
            row = approvers.higherEntry(row.getKey());
        }
        if (row == null) {
            return "no one may approve it";
        }
        return "approved by " + row.getValue();
    }
}
