package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.problem;

/** Stage two: the director, who knows the CFO by class. */
public final class Director {

    public String approve(Expense expense) {
        if (expense.amount() <= 50_000 && !expense.submitter().equals("Cem")) {
            return "approved by Cem";
        }
        return new Cfo().approve(expense);
    }
}
