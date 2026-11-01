package dev.kaldiroglu.dp.creational.introduction.bank;

public class BankLoan {
    private double principal;
    private double rate;
    private int months;
    private double totalRepayment;

    // If we use a standard constructor, the user has to calculate 'totalRepayment' manually.
    // This is "Easy to create" (just a constructor) but "Hard to use" (user must know math).
    public BankLoan(double principal, double rate, int months, double totalRepayment) {
        // A tolerance, because two double values that should be equal often differ slightly.
        if (Math.abs(totalRepayment - (principal + (principal * rate * months))) > 0.005) {
            throw new IllegalArgumentException("Math doesn't add up!");
        }
        this.principal = principal;
        this.rate = rate;
        this.months = months;
        this.totalRepayment = totalRepayment;
    }

    public double getTotalRepayment() { return totalRepayment; }
}
