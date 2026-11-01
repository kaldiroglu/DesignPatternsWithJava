package dev.kaldiroglu.dp.creational.introduction.bank;

public class SecureLoanMain {
    public static void main(String[] args) {
        SecureLoan loan = SecureLoan.create(1000, 0.01, 12);
        System.out.println("Total: " + loan.getTotalRepayment());

        try {
            SecureLoan.create(-1000, 0.01, 12);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }
}
