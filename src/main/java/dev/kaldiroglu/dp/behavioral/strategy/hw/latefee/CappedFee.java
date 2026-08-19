package dev.kaldiroglu.dp.behavioral.strategy.hw.latefee;

/** The daily rate, but never more than the item is worth replacing. */
public final class CappedFee implements FeeRule {

    private final int cap;

    public CappedFee(int cap) {
        this.cap = cap;
    }

    @Override
    public String name() {
        return "CAPPED";
    }

    @Override
    public int charge(Loan loan) {
        return Math.min(cap, loan.daysLate() * loan.dailyRate());
    }
}
