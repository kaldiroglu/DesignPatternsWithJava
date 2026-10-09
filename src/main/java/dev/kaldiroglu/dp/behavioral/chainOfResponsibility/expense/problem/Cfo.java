package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.problem;

/** Stage two: the CFO, the last in the line. */
public final class Cfo {

    public String approve(Expense expense) {
        if (expense.amount() <= 200_000 && !expense.submitter().equals("Deniz")) {
            return "approved by Deniz";
        }
        return "no one may approve it";
    }
}
