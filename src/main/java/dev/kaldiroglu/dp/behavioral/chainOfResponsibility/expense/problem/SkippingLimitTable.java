package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.problem;

import java.util.Map;
import java.util.TreeMap;

/**
 * Stage three, repaired in the table: when the approver the table finds is the submitter,
 * the table moves on to the next higher limit.
 * <p>
 * The answers are now right. But one method still decides for every approver: an approver
 * here is only a name, so a rule for one approver puts that name in {@link #approve}. The
 * loop walks the rows in order of limit, as a chain walks its links, but no approver decides
 * anything.
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
