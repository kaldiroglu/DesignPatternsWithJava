package dev.kaldiroglu.dp.creational.introduction.bank;

/** No setters, and the total is calculated, so no stored value can disagree with the others. */
public record LoanRecord(double principal, double rate, int months) {

    public LoanRecord {
        if (principal <= 0) throw new IllegalArgumentException("Principal must be positive");
        if (rate < 0) throw new IllegalArgumentException("Rate cannot be negative");
        if (months < 1) throw new IllegalArgumentException("Term must be at least 1 month");
    }

    public double totalRepayment() {
        return principal + (principal * rate * months);
    }
}
