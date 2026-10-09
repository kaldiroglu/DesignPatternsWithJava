package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.problem;

import java.util.List;

/**
 * Runs the six expenses through the three stages. Stages one and two keep the rule; stage
 * three lets Burak approve his own 5,000 and Cem his own 30,000.
 */
public final class Main {

    public static void main(String[] args) {
        List<Expense> expenses = List.of(
                new Expense("Emre", 800, "books"),
                new Expense("Emre", 5_000, "a laptop"),
                new Expense("Burak", 5_000, "a conference"),
                new Expense("Emre", 30_000, "a server"),
                new Expense("Cem", 30_000, "a training course"),
                new Expense("Emre", 500_000, "a new office"));

        ApprovalService service = new ApprovalService();
        TeamLead lead = new TeamLead();
        LimitTable table = new LimitTable()
                .add("Elif", 1_000).add("Burak", 10_000).add("Cem", 50_000).add("Deniz", 200_000);

        for (Expense expense : expenses) {
            String third = table.approve(expense);
            boolean own = third.equals("approved by " + expense.submitter());
            System.out.println(expense.submitter() + ", " + expense.amount() + ", " + expense.purpose());
            System.out.println("  stage one:   " + service.approve(expense));
            System.out.println("  stage two:   " + lead.approve(expense));
            System.out.println("  stage three: " + third + (own ? "  <- his own expense" : ""));
        }
    }
}
