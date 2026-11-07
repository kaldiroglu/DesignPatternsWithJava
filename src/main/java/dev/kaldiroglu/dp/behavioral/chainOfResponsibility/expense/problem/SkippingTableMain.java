package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.problem;

/**
 * Stage three repaired in the table: Burak's 5,000 goes to Cem, and Cem's 30,000 goes to
 * Deniz, as the rule says.
 */
public final class SkippingTableMain {

    public static void main(String[] args) {
        SkippingLimitTable table = new SkippingLimitTable()
                .add("Elif", 1_000).add("Burak", 10_000).add("Cem", 50_000).add("Deniz", 200_000);
        for (Expense expense : SampleExpenses.all()) {
            System.out.println(SampleExpenses.header(expense));
            System.out.println("  skipping table: " + table.approve(expense));
        }
    }
}
