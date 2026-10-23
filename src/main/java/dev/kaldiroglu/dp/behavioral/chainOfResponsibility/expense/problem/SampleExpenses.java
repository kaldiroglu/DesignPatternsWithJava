package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.problem;

import java.util.List;

/** The six expenses that every stage runs on. */
final class SampleExpenses {

    private SampleExpenses() {
    }

    static List<Expense> all() {
        return List.of(
                new Expense("Emre", 800, "books"),
                new Expense("Emre", 5_000, "a laptop"),
                new Expense("Burak", 5_000, "a conference"),
                new Expense("Emre", 30_000, "a server"),
                new Expense("Cem", 30_000, "a training course"),
                new Expense("Emre", 500_000, "a new office"));
    }

    static String header(Expense expense) {
        return expense.submitter() + ", " + expense.amount() + ", " + expense.purpose();
    }
}
