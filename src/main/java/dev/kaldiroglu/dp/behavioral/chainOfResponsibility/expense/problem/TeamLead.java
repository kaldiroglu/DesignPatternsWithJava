package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.problem;

/**
 * Stage two: each approver decides, and calls the next one by class.
 * <p>
 * The rules are now with the people they belong to. But {@code TeamLead} creates a
 * {@code Manager}, the manager creates a {@code Director}, and so on: the order is fixed in
 * the code. A finance check between the manager and the director is an edit to
 * {@code Manager}.
 */
public final class TeamLead {

    public String approve(Expense expense) {
        if (expense.amount() <= 1_000 && !expense.submitter().equals("Elif")) {
            return "approved by Elif";
        }
        return new Manager().approve(expense);
    }
}
