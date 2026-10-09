package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.solution;

/**
 * The <b>Handler</b>: one link in the chain. It knows only the next link, by this type.
 * <p>
 * {@link #handle} asks this link first. If it does not handle the expense, the expense goes
 * to the next link. If there is no next link, nobody handled it — GoF consequence 3
 * (receipt isn't guaranteed) — and the chain says so instead of failing.
 */
public abstract class ExpenseHandler {

    private ExpenseHandler next;

    /** Links the next handler and returns it, so a chain can be written in one line. */
    public ExpenseHandler then(ExpenseHandler next) {
        this.next = next;
        return next;
    }

    public final String handle(Expense expense) {
        String answer = tryToHandle(expense);
        if (answer != null) {
            return answer;
        }
        if (next == null) {
            return "no one may approve it";
        }
        return next.handle(expense);
    }

    /** Returns the answer, or {@code null} to pass the expense on. */
    protected abstract String tryToHandle(Expense expense);
}
