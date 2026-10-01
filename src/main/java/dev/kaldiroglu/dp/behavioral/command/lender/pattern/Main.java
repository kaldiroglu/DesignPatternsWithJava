package dev.kaldiroglu.dp.behavioral.command.lender.pattern;


public class Main {
    public static void main(String[] args) {
        Command command = new Borrower();

        Lender lender = new Lender();
        lender.lend(command, 1000);

        command = new TaxOffice();
        lender.lend(command, 2000);
    }
}
