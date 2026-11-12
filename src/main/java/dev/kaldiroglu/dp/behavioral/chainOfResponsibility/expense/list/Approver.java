package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.list;

import dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.solution.Expense;

/** Approves if the amount is within its limit and the expense is not its own. */
public record Approver(String name, int limit) implements Link {

    @Override
    public String tryToHandle(Expense expense) {
        if (expense.amount() <= limit && !expense.submitter().equals(name)) {
            return "approved by " + name;
        }
        return null;
    }
}
