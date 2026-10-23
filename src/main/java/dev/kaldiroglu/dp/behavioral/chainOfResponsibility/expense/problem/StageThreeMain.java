package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.problem;

/**
 * Stage three: a table of limits chooses by amount alone, so Burak approves his own 5,000
 * and Cem his own 30,000.
 */
public final class StageThreeMain {

    public static void main(String[] args) {
        LimitTable table = new LimitTable()
                .add("Elif", 1_000).add("Burak", 10_000).add("Cem", 50_000).add("Deniz", 200_000);
        for (Expense expense : SampleExpenses.all()) {
            String answer = table.approve(expense);
            boolean own = answer.equals("approved by " + expense.submitter());
            System.out.println(SampleExpenses.header(expense));
            System.out.println("  stage three: " + answer + (own ? "  <- his own expense" : ""));
        }
    }
}
