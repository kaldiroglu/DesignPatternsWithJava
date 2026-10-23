package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.problem;

/** Stage one: one method decides who approves each of the six expenses. */
public final class StageOneMain {

    public static void main(String[] args) {
        ApprovalService service = new ApprovalService();
        for (Expense expense : SampleExpenses.all()) {
            System.out.println(SampleExpenses.header(expense));
            System.out.println("  stage one:   " + service.approve(expense));
        }
    }
}
