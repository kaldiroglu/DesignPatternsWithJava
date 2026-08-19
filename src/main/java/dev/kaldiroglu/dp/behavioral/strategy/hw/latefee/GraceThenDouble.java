package dev.kaldiroglu.dp.behavioral.strategy.hw.latefee;

/**
 * Nothing for the first few days, then twice the rate.
 * <p>
 * The rule that is not a multiplier, which is why {@link FeeRule} is a method rather than a
 * number. A design that had made the fee rule an {@code int ratePerDay} could not express it.
 */
public final class GraceThenDouble implements FeeRule {

    private final int graceDays;

    public GraceThenDouble(int graceDays) {
        this.graceDays = graceDays;
    }

    @Override
    public String name() {
        return "GRACE_THEN_DOUBLE";
    }

    @Override
    public int charge(Loan loan) {
        int chargeable = loan.daysLate() - graceDays;
        return chargeable <= 0 ? 0 : chargeable * loan.dailyRate() * 2;
    }
}
