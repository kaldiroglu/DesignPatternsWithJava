package dev.kaldiroglu.dp.behavioral.command.lender.problem2;

public class Main {
    public static void main(String[] args) {
        Borrower borrower = new ConcreteBorrower1();

        Lender lender = new Lender();
        lender.lend(borrower, 1000);

        borrower = new ConcreteBorrower2();
        lender.lend(borrower, 2000);
    }
}
