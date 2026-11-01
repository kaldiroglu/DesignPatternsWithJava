package dev.kaldiroglu.dp.creational.introduction.bank;

public class ConstructorCheckMain {
    public static void main(String[] args) {
        BankLoan good = new BankLoan(1000, 0.01, 12, 1120);
        System.out.println("Accepted: " + good.getTotalRepayment());

        try {
            new BankLoan(1000, 0.01, 12, 1100);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }
}
