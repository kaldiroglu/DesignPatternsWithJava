package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.list;

import dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.solution.Expense;

import java.util.ArrayList;
import java.util.List;

/** Records every expense and never answers, so the next handler always gets it. */
public final class AuditLog implements Link {

    private final List<Expense> seen = new ArrayList<>();

    @Override
    public String tryToHandle(Expense expense) {
        seen.add(expense);
        return null;
    }

    public List<Expense> seen() {
        return List.copyOf(seen);
    }
}
