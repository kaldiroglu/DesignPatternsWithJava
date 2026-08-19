package dev.kaldiroglu.dp.behavioral.strategy.hw.latefee;

/** The daily rate, every day, with no ceiling. */
public final class StandardFee implements FeeRule {

    @Override
    public String name() {
        return "STANDARD";
    }

    @Override
    public int charge(Loan loan) {
        return loan.daysLate() * loan.dailyRate();
    }
}
