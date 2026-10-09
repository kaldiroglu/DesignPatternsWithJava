package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.problem;

/** Stage two: the manager, who knows the director by class. */
public final class Manager {

    public String approve(Expense expense) {
        if (expense.amount() <= 10_000 && !expense.submitter().equals("Burak")) {
            return "approved by Burak";
        }
        return new Director().approve(expense);
    }
}
