package dev.kaldiroglu.dp.behavioral.strategy.hw.latefee;

/**
 * One overdue item.
 *
 * @param title      what was borrowed
 * @param daysLate   how many days past the due date
 * @param dailyRate  the base charge per day, in minor units
 */
public record Loan(String title, int daysLate, int dailyRate) {

    public Loan {
        if (daysLate < 0) {
            throw new IllegalArgumentException("an item cannot be returned before it is due");
        }
    }
}
