package dev.kaldiroglu.dp.creational.introduction.bank;

public class SettersMain {
    public static void main(String[] args) {
        LoanWithSetters loan = new LoanWithSetters();
        loan.setPrincipal(1000);
        loan.setRate(0.01);
        loan.setMonths(12);
        loan.setTotalRepayment(1100);   // the client's arithmetic is wrong

        System.out.println("Total stored : " + loan.getTotalRepayment());
        System.out.println("Correct total: " + loan.correctTotal());
    }
}
