package dev.kaldiroglu.dp.creational.introduction.bank;

public class LoanWithSetters {
    private double principal;
    private double rate;
    private int months;
    private double totalRepayment;

    public void setPrincipal(double principal) { this.principal = principal; }
    public void setRate(double rate) { this.rate = rate; }
    public void setMonths(int months) { this.months = months; }
    public void setTotalRepayment(double totalRepayment) { this.totalRepayment = totalRepayment; }

    public double getTotalRepayment() { return totalRepayment; }

    /** What the total must be: principal + principal * rate * months. */
    public double correctTotal() { return principal + (principal * rate * months); }
}
