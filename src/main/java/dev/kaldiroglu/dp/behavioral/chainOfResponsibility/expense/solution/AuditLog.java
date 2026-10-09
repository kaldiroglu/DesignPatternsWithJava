package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * A <b>ConcreteHandler</b> that never decides: it records every expense and passes it on.
 * <p>
 * It is put at the front of the chain without changing any approver. A link may do some
 * work and still pass the request on.
 */
public final class AuditLog extends ExpenseHandler {

    private final List<Expense> seen = new ArrayList<>();

    @Override
    protected String tryToHandle(Expense expense) {
        seen.add(expense);
        return null;
    }

    public List<Expense> seen() {
        return List.copyOf(seen);
    }
}
