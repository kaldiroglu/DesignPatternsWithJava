package dev.kaldiroglu.dp.creational.ch01.bank;

public class SecureLoan {
    private final double principal;
    private final double rate;
    private final int months;
    private final double totalRepayment; // Calculated internally

    private SecureLoan(double principal, double rate, int months) {
        // Enforce basic rules via exceptions
        if (principal <= 0) throw new IllegalArgumentException("Principal must be positive");
        if (rate < 0) throw new IllegalArgumentException("Rate cannot be negative");
        if (months < 1) throw new IllegalArgumentException("Term must be at least 1 month");

        this.principal = principal;
        this.rate = rate;
        this.months = months;

        // Enforce the Relationship Invariant automatically
        this.totalRepayment = principal + (principal * rate * months);
    }

    // Static Factory Method: The only way to create the object
    public static SecureLoan create(double principal, double rate, int months) {
        return new SecureLoan(principal, rate, months);
    }

    // Getters only - no setters allowed to protect the invariants!
    public double getTotalRepayment() { return totalRepayment; }
}