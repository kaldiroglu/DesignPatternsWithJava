package dev.kaldiroglu.dp.creational.introduction.bank;

public class LoanRecordMain {
    public static void main(String[] args) {
        LoanRecord loan = new LoanRecord(1000, 0.01, 12);
        System.out.println(loan);
        System.out.println("Total: " + loan.totalRepayment());

        try {
            new LoanRecord(1000, 0.01, 0);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }
}
