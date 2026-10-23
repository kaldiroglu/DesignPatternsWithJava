package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.problem;

/** Stage two: each approver decides, and creates the next one by class. */
public final class StageTwoMain {

    public static void main(String[] args) {
        TeamLead lead = new TeamLead();
        for (Expense expense : SampleExpenses.all()) {
            System.out.println(SampleExpenses.header(expense));
            System.out.println("  stage two:   " + lead.approve(expense));
        }
    }
}
