package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.problem;

/** An expense claim: who spent the money, how much, and what for. */
public record Expense(String submitter, int amount, String purpose) {
}
