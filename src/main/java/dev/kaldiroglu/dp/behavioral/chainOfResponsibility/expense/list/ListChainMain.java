package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.list;

import dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.solution.Expense;

import java.util.List;

/**
 * Runs the six expenses through the chain kept as a list: the audit log first, then the
 * four approvers. The answers are the same as the chain with a next field.
 */
public final class ListChainMain {

    public static void main(String[] args) {
        ApprovalChain chain = new ApprovalChain()
                .then(new AuditLog())
                .then(new Approver("Elif", 1_000))
                .then(new Approver("Burak", 10_000))
                .then(new Approver("Cem", 50_000))
                .then(new Approver("Deniz", 200_000));

        List<Expense> expenses = List.of(
                new Expense("Emre", 800, "books"),
                new Expense("Emre", 5_000, "a laptop"),
                new Expense("Burak", 5_000, "a conference"),
                new Expense("Emre", 30_000, "a server"),
                new Expense("Cem", 30_000, "a training course"),
                new Expense("Emre", 500_000, "a new office"));

        for (Expense expense : expenses) {
            System.out.println(expense.submitter() + ", " + expense.amount() + ", "
                    + expense.purpose() + ": " + chain.handle(expense));
        }
    }
}
