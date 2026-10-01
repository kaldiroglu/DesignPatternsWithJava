package dev.kaldiroglu.dp.behavioral.command.lender.problem1;


public class Main {
    public static void main(String[] args) {
        Borrower borrower = new Borrower();
        Lender lender = new Lender();
        lender.lend(borrower, 1000);
    }
}
