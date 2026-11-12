package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.list;

import dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.solution.Expense;

/**
 * One handler in a chain kept as a list. It decides by its own rule and returns the answer,
 * or {@code null} to let the next handler try. It has no field for the next handler: the
 * list holds the order, and {@link ApprovalChain} does the passing.
 */
public interface Link {

    String tryToHandle(Expense expense);
}
